package graph;

import model.Course;
import tree.AVLTree;

import java.util.*;

/**
 * Class CourseGraph adalah implementasi Graph manual menggunakan adjacency list.
 *
 * Graph yang digunakan adalah Directed Graph karena hubungan prasyarat memiliki arah.
 * Arah edge:
 *      prerequisiteCode -> courseCode
 *
 * Contoh:
 *      IT102 -> IT201
 * Artinya:
 *      IT102 adalah prasyarat untuk mengambil IT201.
 *
 * Class ini juga mengimplementasikan beberapa algoritma graph:
 * 1. DFS untuk menampilkan semua prasyarat dan rantai prasyarat.
 * 2. BFS untuk menelusuri mata kuliah lanjutan.
 * 3. Topological Sort untuk rekomendasi urutan pengambilan.
 * 4. Cycle Detection untuk mendeteksi konflik kurikulum.
 */
public class CourseGraph {
    /**
     * adjacencyList menyimpan edge keluar dari suatu mata kuliah.
     *
     * Format:
     * key   = kode mata kuliah prasyarat.
     * value = daftar mata kuliah yang membutuhkan key sebagai prasyarat.
     *
     * Contoh:
     * IT102 -> [IT201, IT204, IT304]
     */
    private Map<String, List<String>> adjacencyList;

    /**
     * reverseAdjacencyList menyimpan edge masuk ke suatu mata kuliah.
     *
     * Format:
     * key   = kode mata kuliah tujuan.
     * value = daftar prasyarat langsung untuk mata kuliah tersebut.
     *
     * Contoh:
     * IT201 -> [IT102, IT103]
     *
     * Struktur ini dibuat agar pencarian prasyarat langsung lebih mudah.
     */
    private Map<String, List<String>> reverseAdjacencyList;

    /**
     * courseTree adalah referensi ke AVL Tree.
     * Graph hanya menyimpan kode mata kuliah, sedangkan data lengkap Course
     * diambil dari AVL Tree saat ingin ditampilkan.
     */
    private AVLTree courseTree;

    /**
     * Constructor CourseGraph.
     *
     * LinkedHashMap digunakan agar urutan output mengikuti urutan data dimasukkan.
     */
    public CourseGraph(AVLTree courseTree) {
        this.adjacencyList = new LinkedHashMap<>();
        this.reverseAdjacencyList = new LinkedHashMap<>();
        this.courseTree = courseTree;
    }

    /**
     * Menambahkan vertex/mata kuliah ke graph.
     *
     * putIfAbsent digunakan agar jika vertex sudah ada,
     * data lama tidak tertimpa dan tidak dibuat duplikat.
     */
    public void addCourse(String code) {
        adjacencyList.putIfAbsent(code, new ArrayList<>());
        reverseAdjacencyList.putIfAbsent(code, new ArrayList<>());
    }

    /**
     * Menambahkan edge prasyarat ke graph.
     *
     * Format:
     * prerequisiteCode -> courseCode
     *
     * Langkah kerja:
     * 1. Pastikan kedua kode terdaftar sebagai vertex.
     * 2. Cek apakah relasi sudah ada.
     * 3. Tambahkan edge ke adjacencyList dan reverseAdjacencyList.
     * 4. Jalankan cycle detection.
     * 5. Jika muncul siklus, edge dibatalkan.
     */
    public boolean addPrerequisite(String prerequisiteCode, String courseCode) {
        // Pastikan kedua Course ada sebagai vertex di graph.
        addCourse(prerequisiteCode);
        addCourse(courseCode);

        // Menolak edge duplikat.
        if (adjacencyList.get(prerequisiteCode).contains(courseCode)) {
            System.out.println("Relasi " + prerequisiteCode + " -> " + courseCode + " sudah ada.");
            return false;
        }

        // Tambahkan edge arah normal: prasyarat -> mata kuliah tujuan.
        adjacencyList.get(prerequisiteCode).add(courseCode);

        // Tambahkan juga edge arah balik agar pencarian prasyarat lebih mudah.
        reverseAdjacencyList.get(courseCode).add(prerequisiteCode);

        // Setelah edge baru ditambahkan, cek apakah graph menjadi cyclic.
        if (hasCycle()) {
            System.out.println("Relasi ditolak karena menyebabkan siklus prasyarat.");
            System.out.println("Relasi bermasalah: " + prerequisiteCode + " -> " + courseCode);
            displayCyclePath();

            // Rollback edge karena relasi tersebut membuat kurikulum tidak valid.
            adjacencyList.get(prerequisiteCode).remove(courseCode);
            reverseAdjacencyList.get(courseCode).remove(prerequisiteCode);
            return false;
        }

        return true;
    }

    /**
     * Menghapus edge prasyarat.
     *
     * Contoh:
     * removePrerequisite("IT102", "IT201")
     * berarti menghapus relasi IT102 -> IT201.
     */
    public void removePrerequisite(String prerequisiteCode, String courseCode) {
        // Cek apakah vertex prasyarat ada di graph.
        if (!adjacencyList.containsKey(prerequisiteCode)) {
            System.out.println("Kode prasyarat tidak ditemukan di graph.");
            return;
        }

        // Cek apakah edge yang ingin dihapus memang ada.
        if (!adjacencyList.get(prerequisiteCode).contains(courseCode)) {
            System.out.println("Relasi " + prerequisiteCode + " -> " + courseCode + " tidak ditemukan.");
            return;
        }

        // Hapus edge dari adjacencyList.
        adjacencyList.get(prerequisiteCode).remove(courseCode);

        // Hapus edge dari reverseAdjacencyList agar kedua struktur tetap konsisten.
        if (reverseAdjacencyList.containsKey(courseCode)) {
            reverseAdjacencyList.get(courseCode).remove(prerequisiteCode);
        }

        System.out.println("Relasi berhasil dihapus: " + prerequisiteCode + " -> " + courseCode);
    }

    /**
     * Menampilkan graph dalam bentuk adjacency list.
     * Fitur ini berguna untuk membuktikan bahwa graph dibuat manual.
     */
    public void displayGraph() {
        System.out.println("\n=== DAFTAR RELASI PRASYARAT / ADJACENCY LIST ===");

        for (String code : adjacencyList.keySet()) {
            System.out.print(code + " -> ");

            List<String> nextCourses = adjacencyList.get(code);

            if (nextCourses.isEmpty()) {
                System.out.println("-");
            } else {
                System.out.println(nextCourses);
            }
        }
    }

    /**
     * Mengembalikan jumlah vertex/node dalam graph.
     */
    public int getVertexCount() {
        return adjacencyList.size();
    }

    /**
     * Mengembalikan jumlah edge/relasi dalam graph.
     */
    public int getEdgeCount() {
        int count = 0;

        // Jumlah edge adalah total ukuran semua list tetangga.
        for (String vertex : adjacencyList.keySet()) {
            count += adjacencyList.get(vertex).size();
        }

        return count;
    }

    /**
     * Menampilkan prasyarat langsung suatu mata kuliah.
     *
     * Method ini menggunakan reverseAdjacencyList.
     * Contoh:
     * IT201 -> [IT102, IT103]
     */
    public void displayDirectPrerequisites(String courseCode) {
        // Validasi apakah Course ada di AVL Tree.
        Course course = courseTree.search(courseCode);

        if (course == null) {
            System.out.println("Mata kuliah " + courseCode + " tidak ditemukan.");
            return;
        }

        // Ambil daftar prasyarat langsung dari reverse graph.
        List<String> prerequisites = reverseAdjacencyList.get(courseCode);

        System.out.println("\n=== PRASYARAT LANGSUNG ===");
        System.out.println("Mata kuliah: " + course.getCode() + " - " + course.getName());

        if (prerequisites == null || prerequisites.isEmpty()) {
            System.out.println("Tidak memiliki prasyarat langsung / indegree = 0.");
            return;
        }

        // Tampilkan kode dan nama prasyarat.
        for (String prerequisite : prerequisites) {
            Course prerequisiteCourse = courseTree.search(prerequisite);

            if (prerequisiteCourse != null) {
                System.out.println("- " + prerequisiteCourse.getCode() + " - " + prerequisiteCourse.getName());
            } else {
                System.out.println("- " + prerequisite);
            }
        }
    }

    /**
     * Menampilkan semua prasyarat langsung dan tidak langsung.
     *
     * Contoh:
     * Jika IT801 membutuhkan IT701,
     * IT701 membutuhkan IT601,
     * maka IT601 termasuk prasyarat tidak langsung untuk IT801.
     */
    public void displayAllPrerequisites(String courseCode) {
        Course course = courseTree.search(courseCode);

        if (course == null) {
            System.out.println("Mata kuliah " + courseCode + " tidak ditemukan.");
            return;
        }

        // LinkedHashSet digunakan agar data unik dan urutan penemuan tetap terjaga.
        Set<String> visited = new LinkedHashSet<>();

        // DFS berjalan mundur melalui reverseAdjacencyList.
        collectAllPrerequisites(courseCode, visited);

        System.out.println("\n=== SEMUA PRASYARAT LANGSUNG DAN TIDAK LANGSUNG ===");
        System.out.println("Mata kuliah: " + course.getCode() + " - " + course.getName());

        if (visited.isEmpty()) {
            System.out.println("Mata kuliah ini tidak memiliki prasyarat.");
            return;
        }

        for (String prerequisite : visited) {
            Course prerequisiteCourse = courseTree.search(prerequisite);

            if (prerequisiteCourse != null) {
                System.out.println("- " + prerequisiteCourse.getCode() + " - " + prerequisiteCourse.getName());
            } else {
                System.out.println("- " + prerequisite);
            }
        }
    }

    /**
     * DFS rekursif untuk mengumpulkan semua prasyarat.
     *
     * Arah pencarian:
     * courseCode -> prasyarat langsung -> prasyarat dari prasyarat -> dst.
     */
    private void collectAllPrerequisites(String courseCode, Set<String> visited) {
        List<String> prerequisites = reverseAdjacencyList.get(courseCode);

        if (prerequisites == null) {
            return;
        }

        for (String prerequisite : prerequisites) {
            if (!visited.contains(prerequisite)) {
                // Tandai prasyarat sudah dikunjungi agar tidak dicetak berulang.
                visited.add(prerequisite);

                // Lanjut mencari prasyarat dari prasyarat tersebut.
                collectAllPrerequisites(prerequisite, visited);
            }
        }
    }

    /**
     * Menampilkan rantai prasyarat menggunakan DFS.
     *
     * Output berupa jalur dari mata kuliah paling dasar sampai target.
     */
    public void displayPrerequisiteChain(String courseCode) {
        Course course = courseTree.search(courseCode);

        if (course == null) {
            System.out.println("Mata kuliah " + courseCode + " tidak ditemukan.");
            return;
        }

        System.out.println("\n=== RANTAI PRASYARAT DFS ===");
        System.out.println("Target: " + course.getCode() + " - " + course.getName());

        List<String> path = new ArrayList<>();
        displayPrerequisiteChainDFS(courseCode, path, new HashSet<>());
    }

    /**
     * DFS untuk membangun path/rantai prasyarat.
     *
     * visited di method ini dipakai untuk mencegah infinite recursion
     * jika sewaktu-waktu ada data graph yang tidak valid.
     */
    private void displayPrerequisiteChainDFS(String courseCode, List<String> path, Set<String> visited) {
        visited.add(courseCode);
        path.add(courseCode);

        List<String> prerequisites = reverseAdjacencyList.get(courseCode);

        if (prerequisites == null || prerequisites.isEmpty()) {
            // Jika sudah sampai mata kuliah tanpa prasyarat, cetak path.
            printPath(path);
        } else {
            for (String prerequisite : prerequisites) {
                if (!visited.contains(prerequisite)) {
                    displayPrerequisiteChainDFS(prerequisite, path, visited);
                }
            }
        }

        // Backtracking: hapus node terakhir agar path bisa dipakai untuk cabang lain.
        path.remove(path.size() - 1);
        visited.remove(courseCode);
    }

    /**
     * Mencetak path DFS.
     *
     * Karena DFS berjalan dari target ke prasyarat,
     * path perlu dibalik agar output dimulai dari prasyarat paling awal.
     */
    private void printPath(List<String> path) {
        List<String> reversed = new ArrayList<>(path);
        Collections.reverse(reversed);

        for (int i = 0; i < reversed.size(); i++) {
            Course course = courseTree.search(reversed.get(i));

            if (course != null) {
                System.out.print(course.getCode());
            } else {
                System.out.print(reversed.get(i));
            }

            if (i < reversed.size() - 1) {
                System.out.print(" -> ");
            }
        }

        System.out.println();
    }

    /**
     * BFS dari satu mata kuliah.
     *
     * BFS digunakan untuk melihat mata kuliah lanjutan yang dapat dijangkau
     * setelah menyelesaikan mata kuliah awal.
     *
     * Arah BFS mengikuti adjacencyList:
     * prasyarat -> mata kuliah lanjutan.
     */
    public void bfsFromCourse(String startCode) {
        Course startCourse = courseTree.search(startCode);

        if (startCourse == null) {
            System.out.println("Mata kuliah " + startCode + " tidak ditemukan.");
            return;
        }

        System.out.println("\n=== BFS DARI MATA KULIAH " + startCode + " ===");
        System.out.println("Menampilkan mata kuliah lanjutan yang dapat dijangkau setelah menyelesaikan:");
        System.out.println(startCourse.getCode() + " - " + startCourse.getName());

        // visited mencegah vertex dikunjungi lebih dari satu kali.
        Set<String> visited = new LinkedHashSet<>();

        // Queue adalah struktur utama BFS.
        Queue<String> queue = new LinkedList<>();

        visited.add(startCode);
        queue.add(startCode);

        int order = 1;

        // Selama masih ada vertex dalam queue, proses BFS terus berjalan.
        while (!queue.isEmpty()) {
            String current = queue.poll();
            Course currentCourse = courseTree.search(current);

            if (currentCourse != null) {
                System.out.println(order + ". " + currentCourse.getCode() + " - " + currentCourse.getName());
            } else {
                System.out.println(order + ". " + current);
            }

            order++;

            // Ambil semua tetangga atau mata kuliah lanjutan dari current.
            List<String> neighbors = adjacencyList.get(current);

            if (neighbors != null) {
                for (String neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Mengecek apakah graph memiliki cycle/siklus.
     *
     * Siklus pada graph prasyarat berarti kurikulum tidak valid.
     * Contoh konflik:
     * IT401 -> IT402 dan IT402 -> IT401.
     */
    public boolean hasCycle() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        // Cek semua vertex karena graph bisa saja tidak terhubung penuh.
        for (String vertex : adjacencyList.keySet()) {
            if (hasCycleDFS(vertex, visited, recursionStack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * DFS untuk cycle detection.
     *
     * visited:
     * - berisi vertex yang sudah pernah selesai dicek.
     *
     * recursionStack:
     * - berisi vertex yang sedang berada pada jalur DFS aktif.
     * - Jika vertex ditemukan lagi di recursionStack, berarti ada siklus.
     */
    private boolean hasCycleDFS(String vertex, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(vertex)) {
            return true;
        }

        if (visited.contains(vertex)) {
            return false;
        }

        visited.add(vertex);
        recursionStack.add(vertex);

        List<String> neighbors = adjacencyList.get(vertex);

        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (hasCycleDFS(neighbor, visited, recursionStack)) {
                    return true;
                }
            }
        }

        // Vertex selesai diproses, hapus dari recursion stack.
        recursionStack.remove(vertex);
        return false;
    }

    /**
     * Menampilkan salah satu jalur siklus jika graph memiliki konflik.
     * Method ini dipakai agar output lebih jelas saat edge baru ditolak.
     */
    public void displayCyclePath() {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        List<String> path = new ArrayList<>();

        for (String vertex : adjacencyList.keySet()) {
            if (findCycleDFS(vertex, visited, recursionStack, path)) {
                return;
            }
        }

        System.out.println("Tidak ada detail siklus yang ditemukan.");
    }

    /**
     * DFS untuk mencari dan mencetak path siklus.
     */
    private boolean findCycleDFS(String vertex, Set<String> visited, Set<String> recursionStack, List<String> path) {
        if (recursionStack.contains(vertex)) {
            // Jika vertex sudah ada di path aktif, ambil potongan path mulai dari vertex tersebut.
            int startIndex = path.indexOf(vertex);
            System.out.print("Siklus terdeteksi: ");

            for (int i = startIndex; i < path.size(); i++) {
                System.out.print(path.get(i) + " -> ");
            }

            System.out.println(vertex);
            return true;
        }

        if (visited.contains(vertex)) {
            return false;
        }

        visited.add(vertex);
        recursionStack.add(vertex);
        path.add(vertex);

        for (String neighbor : adjacencyList.getOrDefault(vertex, new ArrayList<>())) {
            if (findCycleDFS(neighbor, visited, recursionStack, path)) {
                return true;
            }
        }

        // Backtracking setelah semua neighbor selesai dicek.
        recursionStack.remove(vertex);
        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Menampilkan rekomendasi urutan pengambilan mata kuliah.
     *
     * Method ini memakai hasil Topological Sort.
     * Jika graph memiliki cycle, topological sort tidak dapat dilakukan.
     */
    public void topologicalSort() {
        List<String> result = getTopologicalOrder();

        if (result == null) {
            System.out.println("Topological Sort gagal karena graph memiliki siklus.");
            return;
        }

        System.out.println("\n=== REKOMENDASI URUTAN PENGAMBILAN MATA KULIAH ===");

        int count = 1;
        for (String code : result) {
            Course course = courseTree.search(code);

            if (course != null) {
                System.out.println(count + ". " + course.getCode() + " - " + course.getName()
                        + " | Semester Rekomendasi: " + course.getSemester());
            } else {
                System.out.println(count + ". " + code);
            }

            count++;
        }
    }

    /**
     * Menampilkan hasil Topological Sort, lalu dikelompokkan berdasarkan semester.
     *
     * Catatan:
     * Topological Sort menentukan urutan yang valid berdasarkan prasyarat,
     * sedangkan semesterMap membuat tampilan lebih mudah dibaca.
     */
    public void topologicalSortBySemester() {
        List<String> result = getTopologicalOrder();

        if (result == null) {
            System.out.println("Topological Sort gagal karena graph memiliki siklus.");
            return;
        }

        // TreeMap dipakai agar semester tampil terurut dari kecil ke besar.
        Map<Integer, List<String>> semesterMap = new TreeMap<>();

        for (String code : result) {
            Course course = courseTree.search(code);

            if (course != null) {
                int semester = course.getSemester();
                semesterMap.putIfAbsent(semester, new ArrayList<>());
                semesterMap.get(semester).add(code);
            }
        }

        System.out.println("\n=== REKOMENDASI BERDASARKAN SEMESTER ===");

        for (Integer semester : semesterMap.keySet()) {
            System.out.println("\nSemester " + semester + ":");

            for (String code : semesterMap.get(semester)) {
                Course course = courseTree.search(code);
                if (course != null) {
                    System.out.println("- " + course.getCode() + " - " + course.getName());
                }
            }
        }
    }

    /**
     * Kahn's Algorithm untuk Topological Sort.
     *
     * Langkah:
     * 1. Hitung indegree setiap vertex.
     * 2. Masukkan vertex dengan indegree 0 ke queue.
     * 3. Keluarkan vertex dari queue dan masukkan ke hasil.
     * 4. Kurangi indegree semua neighbor.
     * 5. Jika indegree neighbor menjadi 0, masukkan ke queue.
     * 6. Jika jumlah hasil tidak sama dengan jumlah vertex, berarti ada cycle.
     *
     * Kompleksitas: O(V + E)
     */
    private List<String> getTopologicalOrder() {
        Map<String, Integer> indegree = new LinkedHashMap<>();

        // Inisialisasi semua indegree dengan 0.
        for (String vertex : adjacencyList.keySet()) {
            indegree.put(vertex, 0);
        }

        // Hitung indegree berdasarkan semua edge yang ada.
        for (String vertex : adjacencyList.keySet()) {
            for (String neighbor : adjacencyList.get(vertex)) {
                indegree.put(neighbor, indegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        Queue<String> queue = new LinkedList<>();

        // Vertex dengan indegree 0 berarti tidak punya prasyarat.
        for (String vertex : indegree.keySet()) {
            if (indegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        List<String> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);

            // Setelah current dianggap selesai diambil,
            // kurangi indegree semua mata kuliah lanjutan.
            for (String neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);

                if (indegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Jika tidak semua vertex masuk ke result, berarti masih ada cycle.
        if (result.size() != adjacencyList.size()) {
            return null;
        }

        return result;
    }

    /**
     * Fitur gabungan Tree dan Graph.
     *
     * Alur:
     * 1. Cari data mata kuliah di AVL Tree.
     * 2. Jika ditemukan, tampilkan detail Course.
     * 3. Gunakan Graph untuk menampilkan semua prasyaratnya.
     *
     * Fitur ini membuktikan bahwa Tree dan Graph saling digunakan.
     */
    public void combinedSearchTreeAndGraph(String courseCode) {
        System.out.println("\n=== FITUR GABUNGAN AVL TREE DAN GRAPH ===");

        Course course = courseTree.search(courseCode);

        if (course == null) {
            System.out.println("Mata kuliah " + courseCode + " tidak ditemukan di AVL Tree.");
            return;
        }

        System.out.println("\nData ditemukan di AVL Tree:");
        course.displayDetail();

        System.out.println("\nData prasyarat dari Graph:");
        displayAllPrerequisites(courseCode);
    }
}
