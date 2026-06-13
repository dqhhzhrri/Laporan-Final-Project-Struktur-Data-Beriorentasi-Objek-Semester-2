package tree;

import model.Course;

/**
 * Class AVLTree adalah implementasi Tree manual tanpa library siap pakai.
 *
 * AVL Tree dipilih karena selalu menjaga tinggi tree tetap seimbang.
 * Dengan tree yang seimbang, operasi utama seperti insert, search, dan delete
 * memiliki kompleksitas O(log n).
 *
 * Pada project ini, AVL Tree digunakan untuk menyimpan mata kuliah berdasarkan kode.
 * Contoh kode: IT101, IT201, IT801.
 */
public class AVLTree {
    // Root adalah node paling atas dari AVL Tree.
    private AVLNode root;

    /**
     * Constructor AVLTree.
     * Saat tree baru dibuat, root masih null karena belum ada data.
     */
    public AVLTree() {
        this.root = null;
    }

    /**
     * Method public untuk insert Course ke AVL Tree.
     * Method ini hanya menerima data, lalu memanggil method rekursif insert().
     */
    public void insert(Course course) {
        root = insert(root, course);
    }

    /**
     * Method rekursif untuk memasukkan Course ke posisi yang benar.
     *
     * Aturan BST:
     * - Jika kode baru lebih kecil dari kode node saat ini, masuk ke kiri.
     * - Jika kode baru lebih besar dari kode node saat ini, masuk ke kanan.
     * - Jika kode sama, data ditolak karena kode mata kuliah harus unik.
     *
     * Setelah insert, tinggi node diperbarui dan tree diseimbangkan kembali.
     */
    private AVLNode insert(AVLNode node, Course course) {
        // Jika posisi kosong ditemukan, buat node baru.
        if (node == null) {
            return new AVLNode(course);
        }

        // Membandingkan kode mata kuliah secara alfabetis.
        int compare = course.getCode().compareTo(node.course.getCode());

        if (compare < 0) {
            // Kode lebih kecil masuk ke subtree kiri.
            node.left = insert(node.left, course);
        } else if (compare > 0) {
            // Kode lebih besar masuk ke subtree kanan.
            node.right = insert(node.right, course);
        } else {
            // Kode sama berarti duplikat, sehingga data tidak dimasukkan.
            System.out.println("Kode " + course.getCode() + " sudah ada. Data ditolak.");
            return node;
        }

        // Setelah insert, tinggi node harus diperbarui.
        updateHeight(node);

        // Balance factor = tinggi kiri - tinggi kanan.
        int balance = getBalance(node);

        // Left Left Case:
        // Node berat ke kiri, dan data baru masuk ke kiri dari anak kiri.
        if (balance > 1 && course.getCode().compareTo(node.left.course.getCode()) < 0) {
            return rotateRight(node);
        }

        // Right Right Case:
        // Node berat ke kanan, dan data baru masuk ke kanan dari anak kanan.
        if (balance < -1 && course.getCode().compareTo(node.right.course.getCode()) > 0) {
            return rotateLeft(node);
        }

        // Left Right Case:
        // Node berat ke kiri, tetapi data baru masuk ke kanan dari anak kiri.
        // Solusi: rotasi kiri pada anak kiri, lalu rotasi kanan pada node.
        if (balance > 1 && course.getCode().compareTo(node.left.course.getCode()) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Left Case:
        // Node berat ke kanan, tetapi data baru masuk ke kiri dari anak kanan.
        // Solusi: rotasi kanan pada anak kanan, lalu rotasi kiri pada node.
        if (balance < -1 && course.getCode().compareTo(node.right.course.getCode()) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        // Jika sudah seimbang, node dikembalikan tanpa rotasi.
        return node;
    }

    /**
     * Method public untuk mencari mata kuliah berdasarkan kode.
     * Mengembalikan Course jika ditemukan, atau null jika tidak ditemukan.
     */
    public Course search(String code) {
        AVLNode result = search(root, code);
        if (result == null) {
            return null;
        }
        return result.course;
    }

    /**
     * Method rekursif untuk search pada AVL Tree.
     *
     * Karena AVL Tree mengikuti aturan BST:
     * - kode lebih kecil dicari ke kiri.
     * - kode lebih besar dicari ke kanan.
     */
    private AVLNode search(AVLNode node, String code) {
        // Jika node null, data tidak ditemukan.
        if (node == null) {
            return null;
        }

        int compare = code.compareTo(node.course.getCode());

        if (compare == 0) {
            // Kode sama berarti data ditemukan.
            return node;
        } else if (compare < 0) {
            // Kode yang dicari lebih kecil, lanjut ke kiri.
            return search(node.left, code);
        } else {
            // Kode yang dicari lebih besar, lanjut ke kanan.
            return search(node.right, code);
        }
    }

    /**
     * Method public untuk menghapus Course dari AVL Tree berdasarkan kode.
     */
    public void delete(String code) {
        root = delete(root, code);
    }

    /**
     * Method rekursif untuk delete node dari AVL Tree.
     *
     * Ada 3 kasus penghapusan:
     * 1. Node tidak punya anak.
     * 2. Node punya 1 anak.
     * 3. Node punya 2 anak.
     *
     * Setelah delete, tree diseimbangkan kembali menggunakan rotasi AVL.
     */
    private AVLNode delete(AVLNode node, String code) {
        if (node == null) {
            System.out.println("Kode " + code + " tidak ditemukan.");
            return null;
        }

        int compare = code.compareTo(node.course.getCode());

        if (compare < 0) {
            // Data yang akan dihapus berada di subtree kiri.
            node.left = delete(node.left, code);
        } else if (compare > 0) {
            // Data yang akan dihapus berada di subtree kanan.
            node.right = delete(node.right, code);
        } else {
            // Node dengan kode yang dicari sudah ditemukan.

            if (node.left == null || node.right == null) {
                // Kasus node punya 0 atau 1 anak.
                AVLNode temp = null;

                if (node.left != null) {
                    temp = node.left;
                } else if (node.right != null) {
                    temp = node.right;
                }

                if (temp == null) {
                    // Tidak punya anak, node langsung dihapus.
                    node = null;
                } else {
                    // Punya 1 anak, node diganti dengan anaknya.
                    node = temp;
                }
            } else {
                // Kasus node punya 2 anak.
                // Gunakan inorder successor, yaitu node terkecil di subtree kanan.
                AVLNode successor = getMinValueNode(node.right);
                node.course = successor.course;

                // Setelah data successor dipindahkan, hapus successor dari subtree kanan.
                node.right = delete(node.right, successor.course.getCode());
            }
        }

        // Jika node menjadi null setelah delete, langsung kembalikan null.
        if (node == null) {
            return null;
        }

        // Update tinggi dan hitung ulang balance factor.
        updateHeight(node);
        int balance = getBalance(node);

        // Left Left Case setelah delete.
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }

        // Left Right Case setelah delete.
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        // Right Right Case setelah delete.
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }

        // Right Left Case setelah delete.
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    /**
     * Mencari node dengan kode terkecil pada subtree tertentu.
     * Digunakan saat delete node yang memiliki 2 anak.
     */
    private AVLNode getMinValueNode(AVLNode node) {
        AVLNode current = node;

        // Node paling kiri adalah node dengan kode terkecil.
        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    /**
     * Menampilkan semua Course secara urut berdasarkan kode.
     * Inorder traversal pada BST menghasilkan urutan ascending.
     */
    public void inorderTraversal() {
        if (root == null) {
            System.out.println("Data mata kuliah kosong.");
            return;
        }

        inorderTraversal(root);
    }

    /**
     * Traversal urutan kiri-root-kanan.
     */
    private void inorderTraversal(AVLNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.println(node.course);
            inorderTraversal(node.right);
        }
    }

    /**
     * Search berdasarkan prefix kode atau nama.
     *
     * Contoh:
     * - Prefix "IT3" akan menampilkan semua mata kuliah dengan kode IT301, IT302, dst.
     * - Prefix "Keamanan" akan menampilkan mata kuliah yang namanya diawali Keamanan.
     *
     * Karena pencarian prefix pada AVL Tree tidak langsung seperti Trie,
     * method ini melakukan traversal seluruh tree.
     */
    public void searchByPrefix(String prefix) {
        boolean[] found = {false};
        String normalizedPrefix = prefix.toUpperCase();
        searchByPrefix(root, normalizedPrefix, found);

        if (!found[0]) {
            System.out.println("Tidak ada mata kuliah dengan prefix/kode/nama: " + prefix);
        }
    }

    /**
     * Method rekursif untuk mencari semua Course yang cocok dengan prefix.
     */
    private void searchByPrefix(AVLNode node, String prefix, boolean[] found) {
        if (node == null) {
            return;
        }

        // Cek subtree kiri terlebih dahulu agar hasil tetap urut berdasarkan kode.
        searchByPrefix(node.left, prefix, found);

        String code = node.course.getCode().toUpperCase();
        String name = node.course.getName().toUpperCase();

        // Cocok jika kode atau nama diawali prefix yang dimasukkan user.
        if (code.startsWith(prefix) || name.startsWith(prefix)) {
            System.out.println(node.course);
            found[0] = true;
        }

        // Lanjut cek subtree kanan.
        searchByPrefix(node.right, prefix, found);
    }

    /**
     * Mengambil tinggi node.
     * Jika node null, tingginya dianggap 0.
     */
    private int height(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Memperbarui tinggi node berdasarkan tinggi anak kiri dan kanan.
     */
    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Menghitung balance factor.
     *
     * Balance factor = tinggi subtree kiri - tinggi subtree kanan.
     * Pada AVL Tree, nilai valid adalah -1, 0, atau 1.
     */
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return height(node.left) - height(node.right);
    }

    /**
     * Rotasi kanan digunakan untuk memperbaiki Left Left Case.
     *
     * Sebelum rotasi:
     *        y
     *       /
     *      x
     *
     * Setelah rotasi:
     *        x
     *         \
     *          y
     */
    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode temp = x.right;

        // Proses rotasi.
        x.right = y;
        y.left = temp;

        // Update tinggi setelah struktur berubah.
        updateHeight(y);
        updateHeight(x);

        // x menjadi root baru dari subtree ini.
        return x;
    }

    /**
     * Rotasi kiri digunakan untuk memperbaiki Right Right Case.
     *
     * Sebelum rotasi:
     *      x
     *       \
     *        y
     *
     * Setelah rotasi:
     *        y
     *       /
     *      x
     */
    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode temp = y.left;

        // Proses rotasi.
        y.left = x;
        x.right = temp;

        // Update tinggi setelah struktur berubah.
        updateHeight(x);
        updateHeight(y);

        // y menjadi root baru dari subtree ini.
        return y;
    }
}
