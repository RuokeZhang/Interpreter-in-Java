import java.util.Stack;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableTable {

    private static int gc = 0;

    HashMap<String, Value> global = new HashMap<>();

    HashMap<String, Function> functions = new HashMap<>();
    Stack<Stack<HashMap<String, Value>>> frames = new Stack<>();

    public void enterScope() {
        // Create a new frame when entering a function
        Stack<HashMap<String, Value>> newFrame = new Stack<>();
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

    void store(String var, Value value) {
        if (isGlobal(var)) {
            global.put(var, value);
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    scope.put(var, value);
                }
            }
        }
    }



    /*
     * public void alias(String lhs, String rhs) {
     * Value rhsValue = getValue(rhs);
     *
     * // Assign rhs's Value object to lhs
     * store(lhs, rhsValue);
     *
     * // Increment reference count since lhs is now an additional reference
     * incrementRefCount(rhs);
     * }
     */

    /*
     * private void store(String var, Value value) {
     * // Assigns the given Value object to the specified variable
     * if (isGlobal(var)) {
     * global.put(var, value);
     * } else {
     * Stack<HashMap<String, Value>> local = frames.peek();
     * Map<String, Value> currentScope = local.peek();
     * currentScope.put(var, value);
     * }
     * }
     */

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

    Value getValue(String var) {
        if (isGlobal(var)) {
            return global.get(var);
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (HashMap<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    return scope.get(var);
                }
            }
        }
        return null;
    }

    int getIntValue(String var) {
        return getValue(var).intValue;
    }

    int[] getArrValue(String var) {
        return getValue(var).arrayValue;
    }

    int getIntValue(String var, int index) {
        return getValue(var).arrayValue[index];
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

    void incrementRefCount(String var) {
        Value val = getValue(var);
        if (val != null && val.arrayValue != null) {
            val.rc++;
        }
    }

    void decrementRefCount(String var) {
        Value val = getValue(var);
        if (val != null && val.arrayValue != null) {
            val.rc--;
            if (val.rc == 0) {
                System.out.println("gc: " + --gc);
            }
        }

    }

    void decrementBlockVariable() {
        List<String> blockVariables = getBlockVariables();
        for (String variable : blockVariables) {
            if (Core.ARRAY == getVariableType(variable)&& getRefCount(variable) > 0) {
                decrementRefCount(variable);
            }
        }
    }

    List<String> getBlockVariables() {
        List<String> blockVariables = new ArrayList<>();
        Stack<HashMap<String, Value>> local = frames.peek();
        Map<String, Value> currentScope = local.peek();
        for (String variable : currentScope.keySet()) {
            blockVariables.add(variable);
        }
        return blockVariables;
    }

    void clearGlobalVariable() {
        // set the ref count of global variables to 0
        for (String var : global.keySet()) {
            if (Core.ARRAY == global.get(var).type&& getRefCount(var) > 0) {
                decrementRefCount(var);
            }
        }
    }

    int getRefCount(String var) {
        Value val = getValue(var);
        if (val != null && val.arrayValue != null) {
            return val.rc;
        }
        return 0;
    }

    void printMessage() {
        System.out.println("gc: " + ++gc);
    }
}
