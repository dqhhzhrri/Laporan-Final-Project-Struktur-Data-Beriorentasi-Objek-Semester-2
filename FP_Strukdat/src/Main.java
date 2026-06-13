import data.CourseSeeder;
import graph.CourseGraph;
import model.Course;
import tree.AVLTree;

import java.util.Scanner;

/**
 * Class Main adalah titik awal program.
 *
 * Fungsi utama class ini:
 * 1. Membuat objek AVLTree dan CourseGraph.
 * 2. Memanggil CourseSeeder untuk mengisi dataset awal.
 * 3. Menyediakan menu CLI untuk mengakses fitur project.
 *
 * Project: Course Prerequisite Planner
 * Dataset: Kurikulum TI ITS dari file Dataset.xlsx.
 */
public class Main {
    // AVL Tree digunakan untuk menyimpan dan mencari Course berdasarkan kode.
    private static AVLTree courseTree = new AVLTree();

    // Directed Graph digunakan untuk menyimpan relasi prasyarat antar mata kuliah.
    private static CourseGraph courseGraph = new CourseGraph(courseTree);

    // Scanner digunakan untuk membaca input user dari terminal.
    private static Scanner scanner = new Scanner(System.in);

    /**
     * Method main dijalankan pertama kali saat program dimulai.
     */
    public static void main(String[] args) {
        // Mengisi data mata kuliah ke AVL Tree dan Graph.
        CourseSeeder.seedCourses(courseTree, courseGraph);

        // Mengisi relasi prasyarat ke Graph.
        CourseSeeder.seedPrerequisites(courseGraph);

        int choice;

        // Program berjalan terus sampai user memilih menu 0.
        do {
            showMenu();
            System.out.print("Pilih menu: ");
            choice = inputInt();

            // switch digunakan untuk menjalankan fitur sesuai pilihan menu.
            switch (choice) {
                case 1:
                    displayAllCourses();
                    break;
                case 2:
                    searchCourseByCode();
                    break;
                case 3:
                    searchCourseByPrefix();
                    break;
                case 4:
                    displayDirectPrerequisites();
                    break;
                case 5:
                    displayAllPrerequisites();
                    break;
                case 6:
                    displayPrerequisiteChain();
                    break;
                case 7:
                    // BFS dari mata kuliah awal yang dipilih user.
                    courseGraph.bfsFromCourse(inputCourseCode("Masukkan kode mata kuliah awal BFS: "));
                    break;
                case 8:
                    // Topological Sort untuk urutan valid berdasarkan prasyarat.
                    courseGraph.topologicalSort();
                    break;
                case 9:
                    // Topological Sort yang ditampilkan per semester rekomendasi.
                    courseGraph.topologicalSortBySemester();
                    break;
                case 10:
                    checkCycle();
                    break;
                case 11:
                    simulateAddCourse();
                    break;
                case 12:
                    addPrerequisiteRelation();
                    break;
                case 13:
                    removePrerequisiteRelation();
                    break;
                case 14:
                    combinedTreeGraphFeature();
                    break;
                case 15:
                    courseGraph.displayGraph();
                    break;
                case 16:
                    deleteCourseFromTree();
                    break;
                case 17:
                    displayDatasetSummary();
                    break;
                case 0:
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }

            // Setelah fitur selesai dijalankan, user menekan ENTER untuk kembali ke menu.
            if (choice != 0) {
                pause();
            }

        } while (choice != 0);
    }

    /**
     * Menampilkan daftar menu program.
     */
    private static void showMenu() {
        System.out.println("\n=================================================");
        System.out.println("        COURSE PREREQUISITE PLANNER");
        System.out.println("        Dataset: Kurikulum TI ITS");
        System.out.println("=================================================");
        System.out.println("1.  Tampilkan semua mata kuliah");
        System.out.println("2.  Cari mata kuliah berdasarkan kode");
        System.out.println("3.  Cari mata kuliah berdasarkan prefix kode/nama");
        System.out.println("4.  Tampilkan prasyarat langsung");
        System.out.println("5.  Tampilkan semua prasyarat langsung & tidak langsung");
        System.out.println("6.  Tampilkan rantai prasyarat DFS");
        System.out.println("7.  BFS dari mata kuliah tertentu");
        System.out.println("8.  Rekomendasi urutan pengambilan Topological Sort");
        System.out.println("9.  Rekomendasi berdasarkan semester");
        System.out.println("10. Deteksi siklus prasyarat");
        System.out.println("11. Simulasi penambahan mata kuliah baru");
        System.out.println("12. Tambah relasi prasyarat");
        System.out.println("13. Hapus relasi prasyarat");
        System.out.println("14. Fitur gabungan AVL Tree dan Graph");
        System.out.println("15. Tampilkan graph adjacency list");
        System.out.println("16. Hapus mata kuliah dari AVL Tree");
        System.out.println("17. Tampilkan ringkasan dataset");
        System.out.println("0.  Keluar");
        System.out.println("=================================================");
    }

    /**
     * Menu 1: Menampilkan seluruh mata kuliah.
     * Data ditampilkan dari AVL Tree menggunakan inorder traversal.
     */
    private static void displayAllCourses() {
        System.out.println("\n=== DAFTAR MATA KULIAH BERDASARKAN KODE ===");
        courseTree.inorderTraversal();
    }

    /**
     * Menu 2: Mencari mata kuliah berdasarkan kode.
     * Proses pencarian menggunakan AVL Tree.
     */
    private static void searchCourseByCode() {
        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        Course course = courseTree.search(code);

        if (course == null) {
            System.out.println("Mata kuliah dengan kode " + code + " tidak ditemukan.");
        } else {
            System.out.println("\nMata kuliah ditemukan:");
            course.displayDetail();
        }
    }

    /**
     * Menu 3: Mencari mata kuliah berdasarkan prefix kode atau nama.
     * Contoh input: IT3 atau Keamanan.
     */
    private static void searchCourseByPrefix() {
        System.out.print("Masukkan prefix kode/nama: ");
        String prefix = scanner.nextLine();

        System.out.println("\n=== HASIL PENCARIAN PREFIX ===");
        courseTree.searchByPrefix(prefix);
    }

    /**
     * Menu 4: Menampilkan prasyarat langsung dari suatu mata kuliah.
     */
    private static void displayDirectPrerequisites() {
        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        courseGraph.displayDirectPrerequisites(code);
    }

    /**
     * Menu 5: Menampilkan seluruh prasyarat langsung dan tidak langsung.
     * Fitur ini memakai DFS pada reverse graph.
     */
    private static void displayAllPrerequisites() {
        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        courseGraph.displayAllPrerequisites(code);
    }

    /**
     * Menu 6: Menampilkan rantai prasyarat menggunakan DFS.
     */
    private static void displayPrerequisiteChain() {
        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        courseGraph.displayPrerequisiteChain(code);
    }

    /**
     * Menu 10: Mengecek apakah graph memiliki siklus.
     * Siklus menunjukkan konflik kurikulum.
     */
    private static void checkCycle() {
        System.out.println("\n=== DETEKSI SIKLUS ===");

        if (courseGraph.hasCycle()) {
            System.out.println("Graph memiliki siklus. Prasyarat tidak valid.");
            courseGraph.displayCyclePath();
        } else {
            System.out.println("Graph tidak memiliki siklus. Prasyarat valid.");
        }
    }

    /**
     * Menu 11: Simulasi penambahan mata kuliah baru.
     *
     * Langkah:
     * 1. User memasukkan data Course baru.
     * 2. Course ditambahkan ke AVL Tree.
     * 3. Course ditambahkan sebagai vertex Graph.
     * 4. User memasukkan prasyarat Course baru.
     * 5. Graph mengecek apakah edge baru menyebabkan cycle.
     */
    private static void simulateAddCourse() {
        System.out.println("\n=== SIMULASI PENAMBAHAN MATA KULIAH BARU ===");

        String code = inputCourseCode("Kode mata kuliah: ");

        // Cek duplikasi kode di AVL Tree.
        if (courseTree.search(code) != null) {
            System.out.println("Kode " + code + " sudah digunakan. Data ditolak.");
            return;
        }

        // Input atribut mata kuliah baru.
        System.out.print("Nama mata kuliah: ");
        String name = scanner.nextLine();

        System.out.print("SKS: ");
        int sks = inputInt();

        System.out.print("Semester rekomendasi: ");
        int semester = inputInt();

        System.out.print("Sifat Wajib/Pilihan: ");
        String sifat = scanner.nextLine();

        System.out.print("Bidang keahlian: ");
        String bidangKeahlian = scanner.nextLine();

        System.out.print("Musim buka Ganjil/Genap/Keduanya: ");
        String musimBuka = scanner.nextLine();

        // Membuat objek Course baru dari input user.
        Course newCourse = new Course(code, name, sks, semester, sifat, bidangKeahlian, musimBuka);

        // Tambahkan Course ke AVL Tree dan Graph.
        courseTree.insert(newCourse);
        courseGraph.addCourse(code);

        System.out.print("Berapa jumlah prasyarat untuk mata kuliah ini? ");
        int totalPrerequisite = inputInt();

        // Input semua prasyarat Course baru.
        for (int i = 1; i <= totalPrerequisite; i++) {
            String prerequisite = inputCourseCode("Masukkan kode prasyarat ke-" + i + ": ");

            if (courseTree.search(prerequisite) == null) {
                System.out.println("Kode " + prerequisite + " tidak ditemukan. Relasi dilewati.");
            } else {
                courseGraph.addPrerequisite(prerequisite, code);
            }
        }

        System.out.println("\nMata kuliah baru berhasil disimulasikan.");
        newCourse.displayDetail();

        System.out.println("\nRekomendasi baru:");
        courseGraph.topologicalSortBySemester();
    }

    /**
     * Menu 12: Menambahkan relasi prasyarat baru.
     * Sistem akan menolak edge jika menyebabkan cycle.
     */
    private static void addPrerequisiteRelation() {
        System.out.println("\n=== TAMBAH RELASI PRASYARAT ===");
        System.out.println("Format: A -> B berarti A adalah prasyarat untuk B");

        String prerequisiteCode = inputCourseCode("Masukkan kode prasyarat A: ");
        String courseCode = inputCourseCode("Masukkan kode mata kuliah B: ");

        // Validasi kedua kode harus ada di AVL Tree.
        if (courseTree.search(prerequisiteCode) == null) {
            System.out.println("Kode prasyarat " + prerequisiteCode + " tidak ditemukan.");
            return;
        }

        if (courseTree.search(courseCode) == null) {
            System.out.println("Kode mata kuliah " + courseCode + " tidak ditemukan.");
            return;
        }

        boolean success = courseGraph.addPrerequisite(prerequisiteCode, courseCode);

        if (success) {
            System.out.println("Relasi berhasil ditambahkan: " + prerequisiteCode + " -> " + courseCode);
        }
    }

    /**
     * Menu 13: Menghapus relasi prasyarat dari graph.
     */
    private static void removePrerequisiteRelation() {
        System.out.println("\n=== HAPUS RELASI PRASYARAT ===");

        String prerequisiteCode = inputCourseCode("Masukkan kode prasyarat: ");
        String courseCode = inputCourseCode("Masukkan kode mata kuliah tujuan: ");

        courseGraph.removePrerequisite(prerequisiteCode, courseCode);
    }

    /**
     * Menu 14: Fitur gabungan AVL Tree dan Graph.
     *
     * Data mata kuliah dicari dari AVL Tree,
     * lalu prasyaratnya dicari menggunakan Graph.
     */
    private static void combinedTreeGraphFeature() {
        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        courseGraph.combinedSearchTreeAndGraph(code);
    }

    /**
     * Menu 16: Menghapus mata kuliah dari AVL Tree.
     *
     * Catatan:
     * Method ini hanya menghapus data dari Tree, bukan dari Graph.
     * Fitur ini disertakan untuk memenuhi operasi Tree selain insert dan search.
     */
    private static void deleteCourseFromTree() {
        System.out.println("\n=== HAPUS MATA KULIAH DARI AVL TREE ===");
        System.out.println("Catatan: fitur ini hanya menghapus dari AVL Tree, bukan dari Graph.");

        String code = inputCourseCode("Masukkan kode mata kuliah: ");
        courseTree.delete(code);

        System.out.println("Jika kode ditemukan, data telah dihapus dari AVL Tree.");
    }

    /**
     * Menu 17: Menampilkan ringkasan dataset dan struktur data yang digunakan.
     */
    private static void displayDatasetSummary() {
        System.out.println("\n=== RINGKASAN DATASET ===");
        System.out.println("Jumlah node / mata kuliah : " + courseGraph.getVertexCount());
        System.out.println("Jumlah edge / relasi      : " + courseGraph.getEdgeCount());
        System.out.println("Struktur Tree             : AVL Tree manual");
        System.out.println("Struktur Graph            : Directed Graph dengan adjacency list manual");
        System.out.println("Algoritma Graph           : DFS, BFS, Topological Sort, Cycle Detection");
    }

    /**
     * Helper input untuk kode mata kuliah.
     *
     * trim() menghapus spasi di awal/akhir.
     * toUpperCase() membuat input menjadi huruf besar agar konsisten.
     */
    private static String inputCourseCode(String message) {
        System.out.print(message);
        return scanner.nextLine().trim().toUpperCase();
    }

    /**
     * Helper input angka.
     * Method ini mencegah program error jika user memasukkan selain angka.
     */
    private static int inputInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Input harus berupa angka. Masukkan ulang: ");
            scanner.next();
        }

        int value = scanner.nextInt();

        // Membersihkan karakter ENTER setelah input angka.
        scanner.nextLine();
        return value;
    }

    /**
     * Memberi jeda sebelum kembali ke menu utama.
     */
    private static void pause() {
        System.out.println("\nTekan ENTER untuk kembali ke menu...");
        scanner.nextLine();
    }
}
