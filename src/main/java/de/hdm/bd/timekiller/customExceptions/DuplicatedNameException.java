package de.hdm.bd.timekiller.customExceptions;

public class DuplicatedNameException extends Exception{
    public DuplicatedNameException(){
        super("Whoopsie that's a duplicate." );
    }
}
