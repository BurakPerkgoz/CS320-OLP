package olp.utils;

import olp.database.Connection;

import java.sql.ResultSet;
import java.util.*;
import java.util.regex.*;

public class PrerequisiteParser {


    public static String normalize(String s) {
        return s.toUpperCase()

                .replaceAll("\\d+\\s*(AKTS|ECTS|CREDITS?)", " ")
                .replaceAll("MIN\\.?\\s*\\d+(\\.\\d+)?\\s*GNO", " ")
                .replaceAll("\\d+(\\.\\d+)?\\s*GNO", " ")

                .replaceAll("\\bVEYA\\b", " OR ")
                .replaceAll("\\bOR\\b", " OR ")
                .replace("/", " OR ")

                .replaceAll("\\bVE\\b", " AND ")
                .replaceAll("\\bAND\\b", " AND ")
                .replace("&", " AND ")
                .replace(",", " AND ")

                .replaceAll("\\s+", " ")
                .trim();
    }

    enum TokenType { COURSE, AND, OR, LPAREN, RPAREN }

    static class Token {
        TokenType type;
        String value;
        Token(TokenType t, String v) { type = t; value = v; }
    }

    static List<Token> tokenize(String s) {
        List<Token> tokens = new ArrayList<>();
        Pattern p = Pattern.compile(
                "\\(|\\)|\\bAND\\b|\\bOR\\b|\\b[A-Z]{2,5}\\s?\\d{3}\\b"
        );
        Matcher m = p.matcher(s);

        while (m.find()) {
            String t = m.group();
            if (t.equals("(")) tokens.add(new Token(TokenType.LPAREN, t));
            else if (t.equals(")")) tokens.add(new Token(TokenType.RPAREN, t));
            else if (t.equals("AND")) tokens.add(new Token(TokenType.AND, t));
            else if (t.equals("OR")) tokens.add(new Token(TokenType.OR, t));
            else tokens.add(new Token(TokenType.COURSE, t.replaceAll("\\s+", " ")));
        }
        return tokens;
    }

    public static abstract class Expr {}

    static class CourseExpr extends Expr {
        String course;
        CourseExpr(String c) { course = c; }
    }

    static class AndExpr extends Expr {
        Expr left, right;
        AndExpr(Expr l, Expr r) { left = l; right = r; }
    }

    static class OrExpr extends Expr {
        Expr left, right;
        OrExpr(Expr l, Expr r) { left = l; right = r; }
    }


    static class Parser {
        List<Token> tokens;
        int pos = 0;

        Parser(List<Token> t) { tokens = t; }

        Expr parse() { return parseOr(); }

        private Expr parseOr() {
            Expr e = parseAnd();
            while (match(TokenType.OR))
                e = new OrExpr(e, parseAnd());
            return e;
        }

        private Expr parseAnd() {
            Expr e = parsePrimary();
            while (match(TokenType.AND))
                e = new AndExpr(e, parsePrimary());
            return e;
        }

        private Expr parsePrimary() {
            if (match(TokenType.COURSE))
                return new CourseExpr(prev().value);

            if (match(TokenType.LPAREN)) {
                Expr e = parseOr();
                consume(TokenType.RPAREN);
                return e;
            }
            throw new RuntimeException("Invalid prerequisite syntax");
        }

        private boolean match(TokenType t) {
            if (check(t)) { pos++; return true; }
            return false;
        }

        private boolean check(TokenType t) {
            return pos < tokens.size() && tokens.get(pos).type == t;
        }

        private Token prev() { return tokens.get(pos - 1); }

        private void consume(TokenType t) {
            if (!match(t)) throw new RuntimeException("Expected " + t);
        }
    }


    public static boolean satisfies(Expr e, Set<String> completed) {
        if (e instanceof CourseExpr)
            return completed.contains(((CourseExpr) e).course);

        if (e instanceof AndExpr)
            return satisfies(((AndExpr) e).left, completed)
                    && satisfies(((AndExpr) e).right, completed);

        if (e instanceof OrExpr)
            return satisfies(((OrExpr) e).left, completed)
                    || satisfies(((OrExpr) e).right, completed);

        return false;
    }


    public static Expr parsePrerequisite(String raw) {
        return new Parser(tokenize(normalize(raw))).parse();
    }
}
