package de.hdm.bd.timekiller.customExceptions;

public class DuplicatedNameException extends Exception{
    public DuplicatedNameException(String s){
        super("Whoopsie that's a duplicate." );
    }
}
