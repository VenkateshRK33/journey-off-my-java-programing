import java.util.*;

public class BentleyOttmann {

    static class Point implements Comparable<Point> {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            if (this.x != o.x) return Double.compare(this.x, o.x);
            return Double.compare(this.y, o.y);
        }
    }

    static class Segment {
        static int nextId = 0;
        int id;
        Point p1, p2;

        Segment(Point p1, Point p2) {
            this.id = nextId++;
            if (p1.compareTo(p2) <= 0) {
                this.p1 = p1;
                this.p2 = p2;
            } else {
                this.p1 = p2;
                this.p2 = p1;
            }
        }

        double yAt(double x) {
            if (p1.x == p2.x) return Math.min(p1.y, p2.y); // Vertical segment
            double t = (x - p1.x) / (p2.x - p1.x);
            return p1.y + t * (p2.y - p1.y);
        }
    }

    static class Event implements Comparable<Event> {
        double x;
        int type; // 0: left endpoint, 1: right endpoint, 2: intersection
        Segment seg1, seg2;
        Point point;

        Event(double x, int type, Segment seg) {
            this.x = x;
            this.type = type;
            this.seg1 = seg;
            this.point = (type == 0) ? seg.p1 : seg.p2;
        }

        Event(Point p, Segment s1, Segment s2) {
            this.x = p.x;
            this.type = 2;
            this.point = p;
            this.seg1 = s1;
            this.seg2 = s2;
        }

        @Override
        public int compareTo(Event o) {
            int c = Double.compare(this.x, o.x);
            if (c != 0) return c;
            return Integer.compare(this.type, o.type);
        }
    }

    static class Intersection {
        Point point;
        Segment seg1, seg2;

        Intersection(Point p, Segment s1, Segment s2) {
            this.point = p;
            this.seg1 = s1;
            this.seg2 = s2;
        }
    }

    static Point intersect(Segment s1, Segment s2) {
        double x1 = s1.p1.x, y1 = s1.p1.y, x2 = s1.p2.x, y2 = s1.p2.y;
        double x3 = s2.p1.x, y3 = s2.p1.y, x4 = s2.p2.x, y4 = s2.p2.y;

        double denom = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (Math.abs(denom) < 1e-10) return null; // Parallel

        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / denom;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / denom;

        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            double x = x1 + t * (x2 - x1);
            double y = y1 + t * (y2 - y1);
            return new Point(x, y);
        }
        return null;
    }

    static class SegmentComparator implements Comparator<Segment> {
        double sweepX;

        SegmentComparator(double x) {
            this.sweepX = x;
        }

        @Override
        public int compare(Segment s1, Segment s2) {
            double y1 = s1.yAt(sweepX);
            double y2 = s2.yAt(sweepX);
            return Double.compare(y1, y2);
        }
    }

    public static List<Intersection> findIntersections(List<Segment> segments) {
        PriorityQueue<Event> eventQueue = new PriorityQueue<>();
        TreeSet<Segment> status = new TreeSet<>(new SegmentComparator(0));
        List<Intersection> intersections = new ArrayList<>();
        double DELTA = 1e-10;

        for (Segment s : segments) {
            eventQueue.offer(new Event(s.p1.x, 0, s));
            eventQueue.offer(new Event(s.p2.x, 1, s));
        }

        while (!eventQueue.isEmpty()) {
            Event e = eventQueue.poll();
            status = new TreeSet<>(new SegmentComparator(e.x));

            if (e.type == 0) {
                status.add(e.seg1);
                Segment above = status.higher(e.seg1);
                Segment below = status.lower(e.seg1);
                if (above != null) {
                    Point p = intersect(e.seg1, above);
                    if (p != null && p.x > e.x) eventQueue.offer(new Event(p, e.seg1, above));
                }
                if (below != null) {
                    Point p = intersect(e.seg1, below);
                    if (p != null && p.x > e.x) eventQueue.offer(new Event(p, e.seg1, below));
                }
            } else if (e.type == 1) {
                Segment s = e.seg1;
                Segment above = status.higher(s);
                Segment below = status.lower(s);
                status.remove(s);
                if (above != null && below != null) {
                    Point p = intersect(above, below);
                    if (p != null && p.x > e.x) eventQueue.offer(new Event(p, above, below));
                }
            } else {
                intersections.add(new Intersection(e.point, e.seg1, e.seg2));
                status.remove(e.seg1);
                status.remove(e.seg2);
                status.add(e.seg2);
                status.add(e.seg1);
                SegmentComparator comp = new SegmentComparator(e.x + DELTA);
                status = new TreeSet<>(comp);
                status.addAll(status);
                Segment above = status.higher(e.seg1);
                Segment below = status.lower(e.seg2);
                if (above != null) {
                    Point p = intersect(e.seg1, above);
                    if (p != null && p.x > e.x) eventQueue.offer(new Event(p, e.seg1, above));
                }
                if (below != null) {
                    Point p = intersect(e.seg2, below);
                    if (p != null && p.x > e.x) eventQueue.offer(new Event(p, e.seg2, below));
                }
            }
        }
        return intersections;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Bentley-Ottmann Intersection Finder!");
        System.out.println("Please enter the number of line segments, followed by the coordinates of each segment.");
        System.out.println("Each segment should be entered as four numbers: x1 y1 x2 y2, where (x1,y1) and (x2,y2) are the endpoints.");
        System.out.println("Note: For best results, avoid vertical segments (where x1 == x2).");
        System.out.println("Enter the number of segments:");
        int numSegments = scanner.nextInt();
        List<Segment> segments = new ArrayList<>();
        for (int i = 0; i < numSegments; i++) {
            System.out.println("Enter segment " + i + ": x1 y1 x2 y2");
            double x1 = scanner.nextDouble();
            double y1 = scanner.nextDouble();
            double x2 = scanner.nextDouble();
            double y2 = scanner.nextDouble();
            segments.add(new Segment(new Point(x1, y1), new Point(x2, y2)));
        }
        List<Intersection> intersections = findIntersections(segments);
        if (intersections.isEmpty()) {
            System.out.println("No intersections found.");
        } else {
            System.out.println("Found " + intersections.size() + " intersections:");
            for (Intersection inter : intersections) {
                System.out.println("Intersection at (" + inter.point.x + ", " + inter.point.y + ") between segment " + inter.seg1.id + " and segment " + inter.seg2.id);
            }
        }
        System.out.println("Thank you for using the Intersection Finder!");
        scanner.close();
    }
}