package casestudy3;

import java.util.*;

// Q2 & Q3: Segregated Role-Based Interfaces
interface Registerable {
    boolean enrollStudent(String studentId);
    int getEnrolledCount();
    int getCapacity();
}

interface Assessable {
    String evaluateGrade(String studentId, double score);
}

interface PrerequisiteCheckable {
    boolean meetsPrerequisites(Set<String> completedCourses);
}

// Q1: Abstract Base Class for shared state and identity
abstract class AbstractCourse implements Registerable {
    protected final String courseCode;
    protected final String title;
    protected final int capacity;
    protected final Set<String> enrolledStudents = new HashSet<>();

    protected AbstractCourse(String courseCode, String title, int capacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.capacity = capacity;
    }

    @Override
    public boolean enrollStudent(String studentId) {
        if (enrolledStudents.size() >= capacity) return false;
        return enrolledStudents.add(studentId);
    }

    @Override
    public int getEnrolledCount() { return enrolledStudents.size(); }

    @Override
    public int getCapacity() { return capacity; }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
}

// Q4: Sealed Class Hierarchy establishing a closed contract of course types
sealed abstract class CourseType extends AbstractCourse
    permits RegularCourse, LabCourse, ElectiveCourse, ResearchCourse {

    protected CourseType(String courseCode, String title, int capacity) {
        super(courseCode, title, capacity);
    }
}

final class RegularCourse extends CourseType implements Assessable, PrerequisiteCheckable {
    private final Set<String> prerequisites;

    public RegularCourse(String code, String title, int capacity, Set<String> prerequisites) {
        super(code, title, capacity);
        this.prerequisites = Set.copyOf(prerequisites);
    }

    @Override
    public boolean meetsPrerequisites(Set<String> completedCourses) {
        return completedCourses.containsAll(prerequisites);
    }

    @Override
    public String evaluateGrade(String studentId, double score) {
        return score >= 50.0 ? "PASS (" + score + ")" : "FAIL";
    }
}

final class LabCourse extends CourseType implements Assessable {
    private final String labRoom;

    public LabCourse(String code, String title, int capacity, String labRoom) {
        super(code, title, capacity);
        this.labRoom = labRoom;
    }

    public String getLabRoom() { return labRoom; }

    @Override
    public String evaluateGrade(String studentId, double score) {
        return score >= 60.0 ? "LAB-PASS" : "LAB-REPEAT";
    }
}

final class ElectiveCourse extends CourseType implements Assessable {
    public ElectiveCourse(String code, String title, int capacity) {
        super(code, title, capacity);
    }

    @Override
    public String evaluateGrade(String studentId, double score) {
        return score >= 40.0 ? "CREDIT-EARNED" : "NO-CREDIT";
    }
}

// ResearchCourse does NOT implement Assessable with numeric exam scores or PrerequisiteCheckable
final class ResearchCourse extends CourseType {
    private final String supervisor;

    public ResearchCourse(String code, String title, int capacity, String supervisor) {
        super(code, title, capacity);
        this.supervisor = supervisor;
    }

    public String getSupervisor() { return supervisor; }
}

public class CourseRegistrationSystem {
    // Exhaustive pattern matching enabled by sealed class
    public static String describeCourse(CourseType course) {
        return switch (course) {
            case RegularCourse r -> "Regular Lecture: " + r.getTitle();
            case LabCourse l -> "Lab Session in " + l.getLabRoom() + ": " + l.getTitle();
            case ElectiveCourse e -> "Open Elective: " + e.getTitle();
            case ResearchCourse res -> "Thesis under " + res.getSupervisor() + ": " + res.getTitle();
        };
    }

    public static void main(String[] args) {
        RegularCourse cs201 = new RegularCourse("CS201", "Object-Oriented Design", 60, Set.of("CS101"));
        LabCourse cs202 = new LabCourse("CS202", "Java Programming Lab", 30, "Lab-3B");
        ResearchCourse cs499 = new ResearchCourse("CS499", "Honors Thesis", 5, "Dr. Rao");

        Set<String> completed = Set.of("CS101");
        if (cs201.meetsPrerequisites(completed)) {
            cs201.enrollStudent("STU-101");
        }
        cs202.enrollStudent("STU-101");
        cs499.enrollStudent("STU-101");

        List<CourseType> catalog = List.of(cs201, cs202, cs499);
        for (CourseType c : catalog) {
            System.out.println(describeCourse(c) + " | Enrolled: " + c.getEnrolledCount());
        }
    }
}
