/*
 * Copyright (c) 2021 Javatar LLC
 * All rights reserved.
 */
package pro.javatar.pipeline.command

/**
 * @author Borys Zora
 * @version 2021-01-09
 */
interface Command extends Serializable {

    List<String> execute() throws Exception

    boolean failOnError()

    String name()

}