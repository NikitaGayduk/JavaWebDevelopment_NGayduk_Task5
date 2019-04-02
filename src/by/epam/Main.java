package by.epam;

import by.epam.javawebtraining.gayduknikita.task05.model.entity.CallCenter;
import by.epam.javawebtraining.gayduknikita.task05.util.ClientGenerator;

public class Main {

    public static void main(String[] args) {
        CallCenter callCenter = new CallCenter(2);
        ClientGenerator clientGenerator = new ClientGenerator(callCenter);
    }
}
