package sLang.objects;

import sLang.types.SLangType;

public class SLangObject implements Cloneable{
    private SLangType type;
    private Object value;

    public SLangType getType(){
        return type;
    }

    public SLangObject(SLangType type, Object value){
        this.type = type;
        this.value = value;
    }

    public Object getValue(){
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean toBool(){
        return (boolean) value;
    }

    public SLangObject getAttribute(String name){
        return type.getAttribute(name);
    }
}
