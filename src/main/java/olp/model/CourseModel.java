package olp.model;

import olp.controller.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseModel {

    public static String buildCoursesHtml(
            List<Course> courses,
            int offset,
            int limit,
            int total
    ) {

        StringBuilder html = new StringBuilder();

        html.append("""
            <div class="dtlms-courses-listing-items">
                <div class="grid-sizer dtlms-column dtlms-one-third"></div>
        """);

        if (total == 0) {
            html.append("</div>");
            return html.toString();
        }

        int totalPages = (total + limit - 1) / limit;
        int currentPage = (offset / limit) + 1;

        for (Course c : courses) {
            html.append(buildCourseHtml(c));
        }

        html.append("</div>");

        if (totalPages > 1) {
            html.append(buildPaginationHtml(currentPage, totalPages));
        }

        return html.toString();
    }

    private static String priceHtml(double credits) {
        if (credits <= 0) {
            return """
                <span class="dtlms-price-status dtlms-free">
                    <span class="fas fa-check"></span>
                    0
                </span>
            """;
        }

        return """
            <span class="dtlms-price-status dtlms-cost">
                <ins>%.2f</ins>
            </span>
        """.formatted(credits);
    }

    private static String cartHtml(double credits, long id) {
        if (credits <= 0) {
            return """
                <a href="#" class="dtlms-login-link">
                    <i class="fas fa-unlock-alt"></i>
                    Login To Take Course
                </a>
            """;
        }

        return """
            <a href="?add-to-cart=%d"
               class="dtlms-button small filled">
                <i class="fas fa-shopping-cart"></i>
                Add to Cart
            </a>
        """.formatted(id);
    }

    private static String buildPaginationHtml(int currentPage, int totalPages) {

        StringBuilder pag = new StringBuilder();
        pag.append("<div class=\"dtlms-pagination dtlms-ajax-pagination\">");

        if (currentPage > 1) {
            pag.append("""
                <div class="prev-post">
                    <a href="#" data-currentpage="%d">
                        <span class="fas fa-caret-left"></span>
                        &nbsp;Prev
                    </a>
                </div>
            """.formatted(currentPage));
        }

        pag.append("<ul class='page-numbers'>");

        List<Integer> pageList = new ArrayList<>();
        int side = 2;

        pageList.add(1);

        for (int i = Math.max(2, currentPage - side);
             i <= Math.min(currentPage + side, totalPages - 1);
             i++) {
            pageList.add(i);
        }

        if (totalPages > 1) {
            pageList.add(totalPages);
        }

        for (int p : pageList) {
            pag.append("<li>");
            if (p == currentPage) {
                pag.append(
                    "<span aria-label=\"Page %d\" aria-current=\"page\" class=\"page-numbers current\">%d</span>"
                        .formatted(p, p)
                );
            } else {
                pag.append(
                    "<a aria-label=\"Page %d\" class=\"page-numbers\" href=\"#\">%d</a>"
                        .formatted(p, p)
                );
            }
            pag.append("</li>");
        }

        pag.append("</ul>");

        if (currentPage < totalPages) {
            pag.append("""
                <div class="next-post">
                    <a href="#" data-currentpage="%d">
                        Next <span class="fas fa-caret-right"></span>
                    </a>
                </div>
            """.formatted(currentPage));
        }

        pag.append("</div>");
        return pag.toString();
    }

    private static String buildCourseHtml(Course c) {

        String description = c.getDescription();

        if (c.getPrerequisite() != null && !c.getPrerequisite().isEmpty()) {
            description += "<p>Prerequisite: " + c.getPrerequisite() + "</p>";
        }

        if (c.getCorequisite() != null && !c.getCorequisite().isEmpty()) {
            description += "<p>Corequisite: " + c.getCorequisite() + "</p>";
        }

        return """
        <div class="dtlms-courselist-item-wrapper dtlms-column dtlms-one-third grid-item type1 post-%d">

            <div class="dtlms-courselist-details">
                <div class="dtlms-courselist-details-inner">
                    <div class="dtlms-courselist-metadata-holder">
                        <h5>
                            <a href="/courses/%s">%s (%s-%s)</a>
                        </h5>
                        <div class="dtlms-courselist-metadata">
                            <p class="dtlms-courselist-tags">
                                <i class="fas fa-tag"></i>
                                <a href="#">%s</a>
                            </p>
                            <p class="dtlms-courselist-faculty">
                                <i class="fas fa-university"></i>
                                %s - %s
                            </p>
                            <p class="dtlms-courselist-instructor">
                                <i class="fas fa-user"></i>
                                %s
                            </p>
                            <p class="dtlms-courselist-curriculum">
                                <i class="fas fa-book"></i>
                                5 Curriculum
                            </p>
                        </div>
                    </div>
                </div>

                <div class="dtlms-courselist-description">
                    %s
                </div>

                <div class="dtlms-courselist-bottom-section">
                    <div class="dtlms-coursedetail-price-details">
                        %s
                    </div>
                    <div class="dtlms-coursedetail-cart-details">
                        %s
                    </div>
                </div>

                <div class="dtlms-courselist-bottom-data">
                    <div class="dtlms-courselist-schedule">
                        <i class="far fa-clock"></i>
                        <span>%s</span>
                    </div>
                </div>
            </div>
        </div>
        """.formatted(
                c.getId(),
                slug(c.getTitle()),
                c.getTitle(),
                c.getCourseNo(),
                c.getSectionNo(),
                c.getSubject(),
                c.getFaculty(),
                c.getPartOfTerm(),
                c.getInstructorFullName(),
                description,
                priceHtml(c.getCredits()),
                cartHtml(c.getCredits(), c.getId()),
                c.getScheduleForPrint()
        );
    }

    private static String slug(String text) {
        return text.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
