package ope;

import apim.client.RestRequest;
import apim.client.TmTxRest;
import apim.client.TplmRest;
import apim.client.VectorRest;
import apim.tplm.LockAction;
import apim.tplm.LockElement;
import apim.tplm.LockElementsDto;
import apim.tplm.LockType;

import java.util.List;
import java.util.Scanner;

public class Operator {

    private static final Scanner input = new Scanner(System.in);

    private final TmTxRest tm;
    private final TplmRest tplm;
    private final VectorRest vector;
    private final RestRequest rest;

    public Operator() {
        rest = new RestRequest();
        tm = new TmTxRest();
        tplm = new TplmRest();
        vector = new VectorRest();
    }

    protected void closeClient() {
        tm.closeClient();
        tplm.closeClient();
        vector.closeClient();
        rest.close();
    }

    protected void successOperation() {

        printMessage("Request for a new transaction ID");
        long tid = tm.txBegin();
        printResult(tid);

        LockElementsDto lockElements = createLockElements(tid);
        printMessage("Try acquire lock elements for transaction " + tid);

        if (!tplm.lockAction(lockElements, LockAction.ACQUIRE.action())) return;
        printResult("Elements acquired");

        doOperation(tid);

        printMessage("Commit transaction " + tid);
        tm.txCommit(tid);

        printMessage("Close transaction " + tid);
        tm.txClose(tid);

        printMessage("Release lock elements for transaction " + tid);
        tplm.lockAction(lockElements, LockAction.RELEASE.action());
    }

    protected void incompleteOperation() {

        printMessage("Request for a new transaction ID");
        long tid = tm.txBegin();
        printResult("Transaction opened : " + tid);

        int v, res;
        int x = 100;

        try {

            v = vector.read(tid, 0);
            System.out.println("Value in position [0] [ACTIVE]: " + v);
            res = v - x;

            Thread.sleep(10);

            vector.write(tid,0, res);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tm.txCommit(tid);
        tm.txClose(tid);

        System.out.println("Transaction ended.");

        v = vector.read(tid, 2);
        System.out.println("Value in position [2] [COMMIT]: " + v);
    }

    protected void errorOperation() {
        long tid = tm.txBegin();
        System.out.println("Transaction opened : " + tid);

        doOperation(tid);
        tm.txRollback(tid);
        tm.txClose(tid);

        System.out.println("Transaction ended.");

        int v = vector.read(tid, 2);
        System.out.println("Value in position [2] [ROLLBACK]: " + v);
    }

    protected void doOperation(long tid) {
        int v, res;
        int x = 100;

        printMessage("Read index 0");
        v = vector.read(tid, 0);
        printResult("Value in position [0] [ACTIVE]: " + v);
        res = v - x;

        printMessage("Write index 0");
        vector.write(tid, 0, res);

        printMessage("Read index 2");
        v = vector.read(tid, 2);
        printResult("Value in position [2] [ACTIVE]: " + v);
        res = v + x;

        printMessage("Write index 2");
        vector.write(tid, 2, res);

    }

    private LockElementsDto createLockElements(long tid) {
        return new LockElementsDto(List.of(
                new LockElement(tid, 0, "http://127.0.0.1:8080", LockType.WRITE.name()),
                new LockElement(tid, 2, "http://127.0.0.1:8080", LockType.WRITE.name())
        ));
    }

    private void printMessage(String msg) {
        System.out.print(msg);
        input.nextLine();
    }

    private void printResult(Object o) {
        System.out.println(o);
    }
}
