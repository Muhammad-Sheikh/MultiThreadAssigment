public class Task1 {

    static class NotePlayer implements Runnable {
        private final String noteWav;
        private final String threadName;

        NotePlayer(String noteWav, String threadName) {
            this.noteWav = noteWav;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            new FilePlayer().play("sounds/" + noteWav);
            System.out.println("sounds/" + noteWav + " played by " + threadName);
        }
    }

    private static boolean isThread1Note(String note) {
        return note.equals("do.wav") || note.equals("mi.wav") || note.equals("sol.wav") || note.equals("do-octave.wav");
    }

    private static boolean isThread2Note(String note) {
        return note.equals("re.wav") || note.equals("fa.wav") || note.equals("la.wav") || note.equals("do-octave.wav");
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Select a task:");
        System.out.println("1. Task 1 - Simple Scale (do, re, mi, fa, sol, la, si, do-octave)");
        System.out.println("2. Task 2 - Twinkle Twinkle Little Star");
        System.out.print("Enter your choice (1 or 2): ");

        int choice = scanner.nextInt();
        scanner.close();

        // Single melody list
        String[] melody;
        if (choice == 1) {
            melody = new String[] { "do.wav", "re.wav", "mi.wav", "fa.wav", "sol.wav", "la.wav", "si.wav", "do-octave.wav" };
            System.out.println("\nPlaying Task 1 - Simple Scale");
        } else {
            melody = new String[] {
                "do.wav","do.wav","sol.wav","sol.wav","la.wav","la.wav","sol.wav","fa.wav","fa.wav","mi.wav","mi.wav","re.wav","re.wav","do.wav",
                "sol.wav","sol.wav","fa.wav","fa.wav","mi.wav","mi.wav","re.wav","sol.wav","sol.wav","fa.wav","fa.wav","mi.wav","mi.wav","re.wav",
                "do.wav","do.wav","sol.wav","sol.wav","la.wav","la.wav","sol.wav","fa.wav","fa.wav","mi.wav","mi.wav","re.wav","re.wav","do.wav"
            };
            System.out.println("\nPlaying Task 2 - Twinkle Twinkle Little Star");
        }

        // Dispatcher: allocate note to appropriate thread; do-octave is special (both)
        for (int i = 0; i < melody.length; i++) {
            String note = melody[i];

                if (note.equals("do-octave.wav")) {
                    Thread t1 = new Thread(new NotePlayer(note, "Thread 1"));
                    Thread t2 = new Thread(new NotePlayer(note, "Thread 2"));
                    // Play Thread 1 first, then Thread 2 after 50ms so we hear both
                    t1.start();
                    try { t1.join(); } catch (InterruptedException e) { System.out.println("Interrupted waiting for Thread 1 do-octave"); }
                    try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                    t2.start();
                    try { t2.join(); } catch (InterruptedException e) { System.out.println("Interrupted waiting for Thread 2 do-octave"); }
                    // small pause between notes so we hear stuff
                    try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                    continue;
                }

            if (isThread1Note(note)) {
                Thread t = new Thread(new NotePlayer(note, "Thread 1"));
                t.start();
                try { t.join(); } catch (InterruptedException e) { System.out.println("Interrupted"); }
            } else if (isThread2Note(note)) {
                Thread t = new Thread(new NotePlayer(note, "Thread 2"));
                t.start();
                try { t.join(); } catch (InterruptedException e) { System.out.println("Interrupted"); }
            } else {
                // If a note doesn't belong to either thread (e.g., si), default to Thread 1
                Thread t = new Thread(new NotePlayer(note, "Thread 1"));
                t.start();
                try { t.join(); } catch (InterruptedException e) { System.out.println("Interrupted"); }
            }

            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }
    }
}
