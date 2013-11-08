package uk.me.dillingham.thematicmap.components;

public abstract class Record
{
    private int recordNumber;

    protected Record(int recordNumber)
    {
        this.recordNumber = recordNumber;
    }

    public int getRecordNumber()
    {
        return recordNumber;
    }
}
