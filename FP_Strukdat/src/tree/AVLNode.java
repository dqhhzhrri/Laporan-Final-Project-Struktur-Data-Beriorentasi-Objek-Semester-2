package tree;

import model.Course;

/**
 * Class AVLNode adalah node untuk AVL Tree.
 *
 * Setiap node menyimpan:
 * - 1 objek Course.
 * - pointer ke anak kiri.
 * - pointer ke anak kanan.
 * - tinggi node untuk menghitung balance factor.
 *
 * Field dibuat package-private agar dapat diakses langsung oleh AVLTree
 * yang berada pada package tree yang sama.
 */
public class AVLNode {
    // Data utama yang disimpan pada node, yaitu mata kuliah.
    Course course;

    // Anak kiri berisi Course dengan kode yang lebih kecil.
    AVLNode left;

    // Anak kanan berisi Course dengan kode yang lebih besar.
    AVLNode right;

    // Tinggi node dipakai untuk menghitung keseimbangan AVL Tree.
    int height;

    /**
     * Constructor node baru.
     * Saat pertama dibuat, node belum memiliki anak dan tinggi awalnya 1.
     */
    public AVLNode(Course course) {
        this.course = course;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
