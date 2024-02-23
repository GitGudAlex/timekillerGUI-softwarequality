package de.hdm.bd.timekiller.customExceptions;

public class IllegalNameException extends Exception{
    public IllegalNameException(String message){
        super("Illegal name: " + message);
    }
    public IllegalNameException(){
        super("Illegal name" );
    }
}
