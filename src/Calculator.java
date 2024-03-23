import java.math.BigDecimal;
import java.util.Scanner;

interface ICalculator {
    BigDecimal evaluate(String expression);
}

public class Calculator implements ICalculator {

    @Override
    public BigDecimal evaluate(String expression) {
        expression = expression.replaceAll("\\s", ""); // Remove white spaces
        return evaluateExpression(expression);
    }

    private BigDecimal evaluateExpression(String expression) {
        // Evaluate expression inside brackets first
        int openIndex = expression.lastIndexOf("(");
        if (openIndex != -1) {
            int closeIndex = expression.indexOf(")", openIndex);
            if (closeIndex == -1)
                throw new IllegalArgumentException("Biểu thức không hợp lệ: Thiếu dấu ngoặc đóng");

            String insideBrackets = expression.substring(openIndex + 1, closeIndex);
            BigDecimal result = evaluateExpression(insideBrackets);
            expression = expression.substring(0, openIndex) + result + expression.substring(closeIndex + 1);
            return evaluateExpression(expression);
        }

        return evaluateSimpleExpression(expression);
    }

    private BigDecimal evaluateSimpleExpression(String expression) {
        // Perform addition and subtraction
        int addIndex = expression.lastIndexOf("+");
        int subIndex = expression.lastIndexOf("-");
        if (addIndex != -1 || subIndex != -1) {
            int operatorIndex = Math.max(addIndex, subIndex);
            String leftOperand = expression.substring(0, operatorIndex);
            String rightOperand = expression.substring(operatorIndex + 1);
            BigDecimal leftValue = evaluateSimpleExpression(leftOperand);
            BigDecimal rightValue = evaluateSimpleExpression(rightOperand);
            char operator = expression.charAt(operatorIndex);

            if (operator == '+') {
                return leftValue.add(rightValue);
            } else {
                return leftValue.subtract(rightValue);
            }
        }

        // Perform multiplication and division
        int mulIndex = expression.lastIndexOf("*");
        int divIndex = expression.lastIndexOf("/");
        if (mulIndex != -1 || divIndex != -1) {
            int operatorIndex = Math.max(mulIndex, divIndex);
            String leftOperand = expression.substring(0, operatorIndex);
            String rightOperand = expression.substring(operatorIndex + 1);
            BigDecimal leftValue = evaluateSimpleExpression(leftOperand);
            BigDecimal rightValue = evaluateSimpleExpression(rightOperand);
            char operator = expression.charAt(operatorIndex);

            if (operator == '*') {
                return leftValue.multiply(rightValue);
            } else {
                if (rightValue.equals(BigDecimal.ZERO))
                    throw new ArithmeticException("Division by zero");
                return leftValue.divide(rightValue, 10, BigDecimal.ROUND_HALF_UP); // 10 decimal places
            }
        }

        // If no operators found, parse and return the value
        return new BigDecimal(expression);
    }
}

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Nhập vào số thứ 1: ");
        double num1 = scanner.nextDouble();

        System.out.print("Nhập vào số thứ 2: ");
        double num2 = scanner.nextDouble();

        System.out.print("Nhập vào phép tính (+, -, *, /): ");
        String operator = scanner.next();

        scanner.close();

        String expression = num1 + " " + operator + " " + num2;
        Calculator calculator = new Calculator();
        try {
            double result = calculator.evaluate(expression).doubleValue();
            System.out.println("Kết quả: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}

