package olp.model;

import java.util.List;

public class GraduationTableModel extends CourseTableModel {
    private final double remainingCredits;

    public GraduationTableModel(List<Course> courses, double remainingCredits) {
        super(courses);
        this.remainingCredits = remainingCredits;
    }

    public double getRemainingCredits() {
        return remainingCredits;
    }

    @Override
    public String render() {
        return super.render() + """
                <div class="graduation-credits">
                    Remaining credits: %.1f
                </div>
                """.formatted(remainingCredits);
    }
}

