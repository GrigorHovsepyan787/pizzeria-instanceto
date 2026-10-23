package org.pizzeria;

import org.pizzeria.common.ApplicationContextHolder;
import org.pizzeria.common.RepositoryTypes;

public class Pizzeria {

    public static void main(String[] args) {
        ApplicationContextHolder applicationContextHolder = new ApplicationContextHolder();
        applicationContextHolder.setRepositoryType(RepositoryTypes.FILE_REPOSITORY);

        ConsoleApplication consoleApplication = new ConsoleApplication(applicationContextHolder);
        consoleApplication.run();
    }

}
