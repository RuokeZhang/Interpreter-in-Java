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

    // Stack to maintain variable local. Each entry in the stack represents a scope
    // with variable names and their data types.
    HashMap<String, Value> global= new HashMap<>();;
    HashMap<String, Function> functions = new HashMap<>();;


    Stack<Stack<HashMap<String, Value>>> frames = new Stack<>();


    public void enterScope() {
        // Create a new frame when entering a function
        Stack<HashMap<String, Value>> newFrame = new Stack<>();
        newFrame.push(new HashMap<>());
        // Push the new function frame to the stack
        frames.push(newFrame);
    }

    /**
     * Enter a new variable scope. This is useful for handling nested code blocks.
     */
    public void enterLocalScope() {
        frames.peek().push(new HashMap<>());
    }

    public void leaveScope() {
        // Pop the current frame
        frames.pop();
    }
    /**
     * Leave the current variable scope, typically when exiting a code block.
     */
    public void leaveLocalScope() {
        Stack<HashMap<String, Value>> local = frames.peek();
        local.pop();
    }

    /**
     * Add a variable to the current scope with its datatype.
     *
     * @param var      Variable name to be added.
     * @param dataType Data type of the variable.
     */
    public void addLocalVariable(String var, Core dataType) {
        if(frames.peek().peek().containsKey(var)){
            System.out.println("ERROR: Variable "+var+" already declared in this scope");
            System.exit(1);
        }
        frames.peek().peek().put(var, new Value(dataType, 0, null));
    }

    public void addGlobalVariable(String var, Core dataType) {
        if(global.containsKey(var)){
            System.out.println("ERROR: Variable "+var+" already declared in this scope");
            System.exit(1);
        }
        global.put(var, new Value(dataType, 0, null));
    }

    /**
     * Check if a variable is declared in the current or any outer scope.
     *
     * @param var Variable name to be checked.
     * @return true if variable exists, false otherwise.
     */
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

    /**
     * Add a function to the global scope.
     *
     * @param function Function to be added.
     */
    public void addFunction(Function function) {
        if(functionExists(function.getFunctionName())){
            System.out.println("ERROR: Function "+function.getFunctionName()+" already declared in this scope");
            System.exit(1);
        }
        functions.put(function.getFunctionName(), function);
    }
    /**
     * Check if a function is declared in the global scope.
     *
     * @param functionName Function name to be checked.
     * @return true if function exists, false otherwise.
     */
    public boolean functionExists(String functionName){
        return functions.containsKey(functionName);
    }

    /**
     * Ensure that a variable is declared, and produce an error if not.
     *
     * @param var Variable name to be checked.
     */
    public void checkVariableDeclared(String var) {
        if (!variableExists(var)) {
            System.out.println("ERROR: This variable is undeclared " + var);
            System.exit(1);
        }
    }

    /**
     * Fetch the data type of a specified variable.
     *
     * @param var Variable name.
     * @return Data type of the variable, or null if not found.
     */
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

    /**
     * Ensure that a variable is of the expected type, and produce an error if
     * there's a mismatch.
     *
     * @param var          Variable name to be checked.
     * @param expectedType Expected data type of the variable.
     */
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
            global.get(var).intValue = value;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    scope.get(var).intValue = value;
                }
            }
        }
    }

    void store(String var, int index, int value) {
        if (isGlobal(var)) {
            if(global.get(var).arrayValue==null){
                System.out.println("ERROR: Array "+var+" is not initialized");
                System.exit(1);
            }
            if(index>=global.get(var).arrayValue.length){
                System.out.println("ERROR: Array "+var+" index out of bounds");
                System.exit(1);
            }
            global.get(var).arrayValue[index] = value;
        } else {
            Stack<HashMap<String, Value>> local = frames.peek();
            for (Map<String, Value> scope : local) {
                if (scope.containsKey(var)) {
                    if(scope.get(var).arrayValue==null){
                        System.out.println("ERROR: Array "+var+" is not initialized");
                        System.exit(1);
                    }
                    if(index>=scope.get(var).arrayValue.length){
                        System.out.println("ERROR: Array "+var+" index out of bounds");
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

    int getIntValue(String var)  {
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

    Function getFunctionByName(String functionName){
        return functions.get(functionName);
    }

}
