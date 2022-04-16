package org.moon.figura.testing;

import org.moon.figura.lua.FiguraLuaState;
import org.moon.figura.lua.LuaUtils;
import org.terasology.jnlua.LuaState;

import java.util.Map;

public class LuaTest {

    public static void test() {
        LuaUtils.setupNativesForLua();

        FiguraLuaState luaState = new FiguraLuaState();
        luaState.openLib(LuaState.Library.BASE);
        luaState.openLib(LuaState.Library.TABLE);
        luaState.openLib(LuaState.Library.STRING);
        luaState.openLib(LuaState.Library.MATH);
        luaState.pop(4); //Pop the four libraries we just put on there

        luaState.pushJavaObject(new TestObject());
        luaState.setGlobal("testObj");

        luaState.pushJavaFunction(state -> {
            if (state.isString(1)) {
                String v = state.toString(1);
                System.out.println(v);
            } else if (state.isNil(1)) {
                System.out.println("nil");
            } else if (state.isBoolean(1)) {
                System.out.println(state.toBoolean(1));
            } else if (state.isJavaObjectRaw(1)) {
                System.out.println(state.toJavaObject(1, Object.class));
            } else if (state.isTable(1)) {
                System.out.println(state.toJavaObject(1, Map.class));
            }
            return 0;
        });
        luaState.setGlobal("println");

        String testCode = "" +
                "testObj:testVarArgs()" +
                "testObj:testVarArgs(1)" +
                "testObj:testVarArgs(1, 1)" +
                "testObj:testVarArgs(1, nil)" +
                "testObj:testVarArgs(nil, nil)" +
                "testObj:testVarArgs(nil)" +
                "testObj:testVarArgs(1, 1, 1)" +
                "testObj:testVarArgs(nil, 1, nil)" +
                "testObj:testVarArgs(1, nil, nil, nil, nil, 1)";

        luaState.load(testCode, "main");
        try {
            luaState.call(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
