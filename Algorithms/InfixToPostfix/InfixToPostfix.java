import java.util.Stack;

class InfixToPostfix {
    String convert(String infix) {
        String postfix = "";
        String current = "";
        Stack<String> stack = new Stack<String>();

        for(int i = 0; i < infix.length(); i++) {
            current = infix.substring(i, i+1);

            if(isOperand(current)) {
                postfix = postfix + current;
            }
            else {
                if(current.equals("(")) {
                    stack.push(current);
                }
                else if(current.equals(")")) {
                    while(!stack.peek().equals("(")) {
                        postfix = postfix + stack.pop();
                    }
                }
                else if(stack.isEmpty() || compareOperators(current, stack.peek()) || stack.contains("(")) {
                    stack.push(current);
                }
                else {
                    while(compareOperators(current, stack.peek())) {
                        postfix = postfix + stack.pop();
                    }

                    stack.push(current);
                }
            }
        }

        while(!stack.isEmpty()) {
            postfix = postfix + stack.pop();
        }

        return postfix;
    }

    boolean isOperand(String element) {
        return !element.matches("[\\Q+-*/()\\E]");
    }

    boolean compareOperators(String current, String peek) {
        if(current.equals("*") || current.equals("/")) {
            if(peek.equals("+") || peek.equals("-")) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        InfixToPostfix ifx = new InfixToPostfix();

        String[] tests = {
            "1+2*3+4"
        };

        for(String test: tests) {
            System.out.println(test + " -> " + ifx.convert(test));
        }
    }
}