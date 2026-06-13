package data;

import graph.CourseGraph;
import model.Course;
import tree.AVLTree;

/**
 * Class CourseSeeder berfungsi sebagai pengisi data awal program.
 *
 * Data di file ini berasal dari dataset Excel yang digunakan pada project.
 * Tujuannya agar program langsung memiliki data saat dijalankan,
 * tanpa perlu membaca input mata kuliah satu per satu dari user.
 *
 * Dataset berisi:
 * - 27 node / mata kuliah.
 * - 41 edge / relasi prasyarat.
 */
public class CourseSeeder {

    /**
     * Method seedCourses memasukkan seluruh data mata kuliah ke:
     * 1. AVL Tree, agar Course dapat dicari berdasarkan kode.
     * 2. Graph, agar Course menjadi vertex/node.
     */
    public static void seedCourses(AVLTree tree, CourseGraph graph) {
        // Format constructor Course:
        // Course(kode, nama, sks, semester, sifat, bidangKeahlian, musimBuka)

        // Mata kuliah tingkat awal / dasar.
        add(tree, graph, new Course("IT101", "Pengantar Teknik Elektro & Informatika", 2, 1, "Wajib", "General", "Keduanya"));
        add(tree, graph, new Course("IT102", "Pemrograman Dasar", 4, 1, "Wajib", "General", "Ganjil"));
        add(tree, graph, new Course("IT103", "Matematika Diskrit", 3, 1, "Wajib", "General", "Ganjil"));
        add(tree, graph, new Course("IT104", "Sistem Digital", 3, 1, "Wajib", "General", "Ganjil"));
        add(tree, graph, new Course("IT105", "Hukum dan Etika Teknologi Informasi", 2, 1, "Wajib", "General", "Ganjil"));

        // Mata kuliah semester menengah awal.
        add(tree, graph, new Course("IT201", "Struktur Data dan Algoritma", 4, 2, "Wajib", "General", "Genap"));
        add(tree, graph, new Course("IT202", "Organisasi & Arsitektur Komputer", 3, 2, "Wajib", "General", "Genap"));
        add(tree, graph, new Course("IT203", "Sistem Operasi", 3, 2, "Wajib", "General", "Genap"));
        add(tree, graph, new Course("IT204", "Basis Data", 4, 2, "Wajib", "General", "Genap"));
        add(tree, graph, new Course("IT205", "Statistika dan Probabilitas", 3, 3, "Wajib", "General", "Ganjil"));

        // Mata kuliah inti tingkat lanjut.
        add(tree, graph, new Course("IT301", "Jaringan Komputer", 4, 3, "Wajib", "Cloud/Jaringan", "Ganjil"));
        add(tree, graph, new Course("IT302", "Keamanan Informasi Dasar", 3, 3, "Wajib", "Cybersecurity", "Ganjil"));
        add(tree, graph, new Course("IT303", "Rekayasa Perangkat Lunak", 3, 3, "Wajib", "General", "Ganjil"));
        add(tree, graph, new Course("IT304", "Pemrograman Web", 3, 4, "Wajib", "Integrasi Sistem", "Genap"));
        add(tree, graph, new Course("IT305", "Integrasi Sistem", 3, 4, "Wajib", "Integrasi Sistem", "Genap"));

        // Mata kuliah bidang cloud, jaringan, cybersecurity, dan IoT.
        add(tree, graph, new Course("IT401", "Komputasi Awan (Cloud Computing)", 3, 4, "Wajib", "Cloud/Jaringan", "Genap"));
        add(tree, graph, new Course("IT402", "Internet of Things (IoT)", 3, 5, "Wajib", "IoT", "Ganjil"));
        add(tree, graph, new Course("IT403", "Keamanan Siber & Uji Penetrasi", 3, 5, "Wajib", "Cybersecurity", "Ganjil"));
        add(tree, graph, new Course("IT404", "Administrasi Sistem dan Jaringan", 3, 5, "Wajib", "Cloud/Jaringan", "Ganjil"));
        add(tree, graph, new Course("IT405", "Keamanan Aplikasi", 3, 6, "Pilihan", "Cybersecurity", "Genap"));

        // Mata kuliah pilihan/lanjutan.
        add(tree, graph, new Course("IT501", "Tata Kelola Teknologi Informasi", 3, 6, "Pilihan", "Integrasi Sistem", "Genap"));
        add(tree, graph, new Course("IT502", "Keamanan Cyber-Physical & Smart City", 3, 6, "Pilihan", "IoT/Cyber", "Genap"));
        add(tree, graph, new Course("IT503", "Forensik Digital", 3, 6, "Pilihan", "Cybersecurity", "Genap"));

        // Mata kuliah akhir studi.
        add(tree, graph, new Course("IT601", "Metodologi Penelitian", 2, 6, "Wajib", "General", "Genap"));
        add(tree, graph, new Course("IT602", "Kerja Praktik", 3, 7, "Wajib", "General", "Keduanya"));
        add(tree, graph, new Course("IT701", "Pra-Tugas Akhir (Proposal)", 2, 7, "Wajib", "General", "Keduanya"));
        add(tree, graph, new Course("IT801", "Tugas Akhir", 6, 8, "Wajib", "General", "Keduanya"));
    }

    /**
     * Method seedPrerequisites memasukkan seluruh edge/relasi prasyarat ke graph.
     *
     * Format edge:
     * graph.addPrerequisite("A", "B");
     * Artinya:
     * A adalah prasyarat untuk mengambil B.
     */
    public static void seedPrerequisites(CourseGraph graph) {
        // Relasi dasar ke mata kuliah semester 2.
        graph.addPrerequisite("IT101", "IT202");
        graph.addPrerequisite("IT102", "IT201");
        graph.addPrerequisite("IT103", "IT201");
        graph.addPrerequisite("IT104", "IT202");
        graph.addPrerequisite("IT102", "IT204");

        // Relasi menuju Sistem Operasi.
        graph.addPrerequisite("IT202", "IT203");
        graph.addPrerequisite("IT201", "IT203");

        // Relasi menuju Jaringan dan Keamanan Informasi.
        graph.addPrerequisite("IT203", "IT301");
        graph.addPrerequisite("IT105", "IT302");
        graph.addPrerequisite("IT203", "IT302");
        graph.addPrerequisite("IT301", "IT302");

        // Relasi menuju Rekayasa Perangkat Lunak dan Web.
        graph.addPrerequisite("IT201", "IT303");
        graph.addPrerequisite("IT204", "IT303");
        graph.addPrerequisite("IT102", "IT304");
        graph.addPrerequisite("IT204", "IT304");

        // Relasi menuju Integrasi Sistem dan Cloud.
        graph.addPrerequisite("IT301", "IT305");
        graph.addPrerequisite("IT303", "IT305");
        graph.addPrerequisite("IT301", "IT401");
        graph.addPrerequisite("IT305", "IT401");

        // Relasi menuju IoT.
        graph.addPrerequisite("IT301", "IT402");
        graph.addPrerequisite("IT202", "IT402");

        // Relasi menuju Keamanan Siber dan Administrasi Sistem.
        graph.addPrerequisite("IT302", "IT403");
        graph.addPrerequisite("IT301", "IT403");
        graph.addPrerequisite("IT203", "IT404");
        graph.addPrerequisite("IT301", "IT404");

        // Relasi menuju mata kuliah pilihan tingkat atas.
        graph.addPrerequisite("IT302", "IT405");
        graph.addPrerequisite("IT304", "IT405");
        graph.addPrerequisite("IT305", "IT501");
        graph.addPrerequisite("IT105", "IT501");
        graph.addPrerequisite("IT402", "IT502");
        graph.addPrerequisite("IT403", "IT502");
        graph.addPrerequisite("IT302", "IT503");
        graph.addPrerequisite("IT203", "IT503");

        // Relasi menuju mata kuliah akhir studi.
        graph.addPrerequisite("IT205", "IT601");
        graph.addPrerequisite("IT303", "IT601");
        graph.addPrerequisite("IT305", "IT602");
        graph.addPrerequisite("IT105", "IT602");
        graph.addPrerequisite("IT601", "IT701");
        graph.addPrerequisite("IT602", "IT701");
        graph.addPrerequisite("IT701", "IT801");
        graph.addPrerequisite("IT401", "IT801");
    }

    /**
     * Helper method untuk menambahkan Course ke dua struktur data sekaligus.
     *
     * 1. tree.insert(course) menyimpan data lengkap Course pada AVL Tree.
     * 2. graph.addCourse(course.getCode()) mendaftarkan kode Course sebagai vertex graph.
     */
    private static void add(AVLTree tree, CourseGraph graph, Course course) {
        tree.insert(course);
        graph.addCourse(course.getCode());
    }
}
