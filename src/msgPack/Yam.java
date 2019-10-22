package msgPack;


import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Yam
{
    private int dest;
    private int cmd;
    private List<Value> args;


    public Yam(int dest, int cmd, List<Value> args)
    {
        this.dest = dest;
        this.cmd = cmd;
        this.args = args;
    }

    public Yam(int dest, int cmd, Value[] args)
    {
        this.dest = dest;
        this.cmd = cmd;
        this.args = new ArrayList<>(Arrays.asList(args));
    }


    public int getDest()
    {
        return dest;
    }

    public List<Value> getArgs()
    {
        return args;
    }

    public int getCmd()
    {
        return cmd;
    }


    public Value getYam()
    {
        return ValueFactory.newArray(ValueFactory.newInteger(this.dest),
                ValueFactory.newInteger(this.cmd),
                ValueFactory.newArray(this.args));
    }
}
