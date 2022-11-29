package com.akawane0813.command;

import com.akawane0813.exception.KeyNotFoundException;

public interface IDatabaseCommands {

    Object execute (Object object) throws KeyNotFoundException;

    Object undo() throws KeyNotFoundException;
}
