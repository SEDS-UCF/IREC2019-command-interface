package msgPack;


import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import java.util.List;

public class Yam {
    public int dest;
    public int cmd;
    public List<Value> args;

    public Yam(int dest, int cmd, List<Value> args) {
        this.dest = dest;
        this.cmd = cmd;
        this.args = args;
    }

    public Value getMsg() {
        return ValueFactory.newArray(ValueFactory.newInteger(dest),
                ValueFactory.newInteger(cmd),
                ValueFactory.newArray(args));
    }
}
