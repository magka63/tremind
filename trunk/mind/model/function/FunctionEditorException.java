package mind.model.function;

public class FunctionEditorException
extends Exception
{
    private int c_row, c_col, c_ts;
    public FunctionEditorException()
    {
	super();
	c_row = 0;
	c_col = 0;
    }

    public FunctionEditorException(String s)
    {
	super(s);
	c_row = 0;
	c_col = 0;
    }

    public FunctionEditorException(String s, int row, int col, int ts)
    {
	super(s);
	c_row = row;
	c_col = col;
	c_ts = ts;
    }
    public int getRow() {
	return c_row;
    }
    public int getCol() {
	return c_col;
    }
    public int getTs() {
	return c_ts;
    }
}
