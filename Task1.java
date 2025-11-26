public class Task1 {

    static final Object lock = new Object();
    static boolean turn = true;

    static class playingTask extends Thread {
        boolean type;
        String[] playbook1 = {"do.wav", "mi.wav", "sol.wav", "si.wav"};
        String[] playbook2 = {"re.wav", "fa.wav", "la.wav"};
        int finished;
        playingTask(boolean type){
            this.type = type;
        }
        public void run(){
            if (type) {// Thread 1
                for (String s : playbook1) {
                    synchronized (lock) {
                        while (!turn) {
                            try { lock.wait(); }
                            catch (InterruptedException ignored) {}
                        }
                        new FilePlayer().play("sounds/" + s);
                        System.out.println("sounds/" + s + " played by Thread 1");
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException ignored) {
                            System.out.println("Something Went Wrong!");
                        }
                        if( s.equals("si.wav")){
                            finished++;
                        }
                        turn = false;
                        lock.notifyAll();
                    }
                }
            } else {// Thread 2
                for (String s : playbook2) {
                    synchronized (lock) {
                        while (turn) {
                            try { lock.wait(); }
                            catch (InterruptedException ignored) {}
                        }
                        new FilePlayer().play("sounds/" + s);
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException ignored) {
                            System.out.println("Something Went Wrong!");
                        }
                        if( s.equals("la.wav")){
                            finished++;
                        }
                        turn = true;
                        lock.notifyAll();
                    }
                }
            }
            synchronized (lock) {
                finished++;
                lock.notifyAll();
                while (finished < 2) {
                    try { lock.wait(); }
                    catch (InterruptedException ignored) {}
                }
            }
            new FilePlayer().play("sounds/do-octave.wav");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new playingTask(true);
        Thread t2 = new playingTask(false);
        t1.start();
        t2.start();
    }
}
