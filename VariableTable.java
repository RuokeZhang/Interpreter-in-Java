import java.util.HashSet;
import java.util.Stack;
import java.util.HashMap;
import java.util.Map;


public class VariableTable {

    static class Value {
        Core type;
        int intValue;
        int[] arrayValue;

        public Value(Core dataType, int intValue, int[] arrValue) {
            this.type = dataType;
            this.intValue = intValue;
            this.arrayValue = arrValue;
        }
    }

    HashMap<String, Value> global = new HashMap<>();

    HashMap<String, Function> functions = new HashMap<>();
    Stack<Stack<HashMap<String, Value>>> frames = new Stack<>();


    public void enterScope() {
        // Create a new frame when entering a function
        Stack<HashMap<String, Value>> newFrame = new Stack<>();
        newFrame.push(new HashMap<>());
        // Push the new function frame to the stack
        frames.push(newFrame);
    }

    public void enterLocalScope() {
        frames.peek().push(new HashMap<>());
    }

    public void leaveScope() {
        // Pop the current frame
        frames.pop();
    }

    public void leaveLocalScope() {
        Stack<HashMap<String, Value>> local = frames.peek();
        local.pop();
    }

    public void addVariable(String var, Core dataType) {
        if (!frames.isEmpty()) {
            frames.peek().peek().put(var, new Value(dataType, 0, null));
        } else {
            global.put(var, new Value(dataType, 0, null));
        }
    }


    public boolean variableExists(String var) {
        if (global.containsKey(var)) {
            return true;
        }
        Stack<HashMap<String, Value>> local = frames.peek();
        for (Map<String, Value> scope : local) {
            if (scope.containsKey(var)) {
                return true;
            }
        }
        return false;
    }

    public void addFunction(Function function) {
        if (functionExists(function.getFunctionName())) {
            System.out.println("ERROR: Function " + function.getFunctionName() + " already declared in this scope");
            System.exit(1);
        }
        functions.put(function.getFunctionName(), function);
    }

    public boolean functionExists(String functionName) {
        return functions.containsKey(functionName);
    }

    public void checkVariableDeclared(String var) {
        if (!variableExists(var)) {
            System.out.println("ERROR: This variable is undeclared " + var);
            System.exit(1);
        }
    }

    public Core getVariableType(String var) {
        if (isGlobal(var)) {
            return global.get(var).type;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    return scope.get(var).type;
                }
            }
        }
        return null;
    }

    // Check if a variable is declared in the global scope.
    public boolean isGlobal(String var) {
        return global.containsKey(var);
    }

    public void checkVariableType(String var, Core expectedType) {
        Core actualType = getVariableType(var);
        if (actualType != expectedType) {
            System.out.println(
                    "ERROR: Variable " + var + " of type " + actualType + " cannot be used as " + expectedType);
            System.exit(1);
        }
    }

    void store(String var, int value) {
        if (isGlobal(var)) {
            if (getVariableType(var) == Core.INTEGER) {
                global.get(var).intValue = value;
            } else {
                global.get(var).arrayValue[0] = value;
            }
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    if (getVariableType(var) == Core.INTEGER) {
                        scope.get(var).intValue = value;
                    } else {
                        scope.get(var).arrayValue[0] = value;
                    }
                }
            }
        }
    }


    void store(String var, int index, int value) {
        if (isGlobal(var)) {
            if (global.get(var).arrayValue == null) {
                System.out.println("ERROR: Array " + var + " is not initialized globally");
                System.exit(1);
            }
            if (index >= global.get(var).arrayValue.length) {
                System.out.println("ERROR: Array " + var + " index out of bounds");
                System.exit(1);
            }
            global.get(var).arrayValue[index] = value;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    if (scope.get(var).arrayValue == null) {
                        System.out.println("ERROR: Array " + var + " is not initialized locally");
                        System.exit(1);
                    }
                    if (index >= scope.get(var).arrayValue.length) {
                        System.out.println("ERROR: Array " + var + " index out of bounds");
                        System.exit(1);
                    }
                    scope.get(var).arrayValue[index] = value;
                }
            }
        }
    }

    void store(String var, int[] value) {
        if (isGlobal(var)) {
            global.get(var).arrayValue = value;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    scope.get(var).arrayValue = value;
                }
            }
        }
    }

    void newArray(String var, int size) {
        if (isGlobal(var)) {
            global.get(var).arrayValue = new int[size];
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    scope.get(var).arrayValue = new int[size];
                }
            }
        }
    }


    int getIntValue(String var) {
        if (isGlobal(var)) {
            return global.get(var).intValue;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    return scope.get(var).intValue;
                }
            }
        }
        return 0;
    }

    int[] getArrValue(String var) {
        if (isGlobal(var)) {
            return global.get(var).arrayValue;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    return scope.get(var).arrayValue;
                }
            }
        }
        return null;
    }

    int getIntValue(String var, int index) {
        if (isGlobal(var)) {

            return global.get(var).arrayValue[index];
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    return scope.get(var).arrayValue[index];
                }
            }
        }
        return 0;
    }

    Function getFunctionByName(String functionName) {
        return functions.get(functionName);
    }

    boolean variableIsInitialized(String var) {
        if (isGlobal(var)) {
            return global.get(var).arrayValue != null;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    if (scope.get(var).arrayValue != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
