package org.codehaus.janino;

import java.util.Stack;

public class TargetTypeStack {

    protected Stack<IClass> access;
    protected Stack<IClass> conversion;

    public void pushAccess(IClass iClass){
        access.push(iClass);
    }

    public IClass forAccess(){
        return access.peek();
    }

    public void pushConversion(IClass iClass){
        access.push(iClass);
    }

    public IClass forConversion(){
        return access.peek();
    }

    public void releaseAccess(){

    }

}
