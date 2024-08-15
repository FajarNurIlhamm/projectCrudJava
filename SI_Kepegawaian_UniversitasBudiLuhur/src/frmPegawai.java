import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class frmPegawai extends JFrame implements ActionListener {
    JLabel lblKdMTK = new JLabel("Id_Pegawai : ");
    JLabel lblNMMTK = new JLabel("Nama : ");
    JLabel lblSKS = new JLabel("Alamat : ");
    JTextField txtKdMTK = new JTextField("");
    JTextField txtNMMTK = new JTextField("");
    JTextField txtSKS = new JTextField("");
    JButton btnTambah = new JButton("TAMBAH");
    JButton btnUbah = new JButton("UBAH");
    JButton btnHapus = new JButton("HAPUS");
    JButton btnBersih = new JButton("BERSIH");

    private Connection connection;
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/si_kepegawaian";
    private final String username = "root";
    private final String password = "";

    public frmPegawai() {
        setTitle(".: Entry Data Mata Kuliah :.");
        setSize(600, 300);
        setLocationRelativeTo(this);

        lblKdMTK.setBounds(20, 30, 100, 25);
        lblNMMTK.setBounds(20, 60, 100, 25);
        lblSKS.setBounds(20, 90, 100, 25);


        txtKdMTK.setBounds(125, 30, 100, 25);
        txtNMMTK.setBounds(125, 60, 100, 25);
        txtSKS.setBounds(125, 90, 100, 25);


        btnTambah.setBounds(20, 150, 100, 25);
        btnUbah.setBounds(130, 150, 100, 25);
        btnHapus.setBounds(240, 150, 100, 25);
        btnBersih.setBounds(350, 150, 100, 25);

        getContentPane().setLayout(null);
        getContentPane().add(lblKdMTK);
        getContentPane().add(lblNMMTK);
        getContentPane().add(lblSKS);


        getContentPane().add(txtKdMTK);
        getContentPane().add(txtNMMTK);
        getContentPane().add(txtSKS);


        getContentPane().add(btnTambah);
        getContentPane().add(btnUbah);
        getContentPane().add(btnHapus);
        getContentPane().add(btnBersih);

        btnTambah.addActionListener(this);
        btnBersih.addActionListener(this);
        btnHapus.addActionListener(this);
        btnUbah.addActionListener(this);

        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to database");
        }

        BERSIH();

        txtKdMTK.addActionListener(this);

        setVisible(true);
    }

    void tambahData() {
        String idPegawai = txtKdMTK.getText().trim();
        String namaPegawai = txtNMMTK.getText().trim();
        String alamat = txtSKS.getText().trim();

        try {
            String query = "INSERT INTO tblpegawai (Id_Pegawai, Nm_Pegawai, Alamat) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idPegawai);
            statement.setString(2, namaPegawai);
            statement.setString(3, alamat);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan ke database");
                BERSIH(); // Membersihkan inputan setelah penambahan data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void ubahData() {
        String idPegawai = txtKdMTK.getText().trim();
        String namaPegawai = txtNMMTK.getText().trim();
        String alamat = txtSKS.getText().trim();


        try {
            String query = "UPDATE tblpegawai SET Nm_Pegawai=?, Alamat=? WHERE Id_Pegawai=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, namaPegawai);
            statement.setString(2, alamat);
            statement.setString(3, idPegawai);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah di database");
                BERSIH(); // Membersihkan inputan setelah perubahan data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void hapusData() {
        String idPegawai = txtKdMTK.getText().trim();

        try {
            String query = "DELETE FROM tblpegawai WHERE Id_Pegawai=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idPegawai);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus dari database");
                BERSIH(); // Membersihkan inputan setelah penghapusan data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void BERSIH() {
        txtKdMTK.setText("");
        txtNMMTK.setText("");
        txtSKS.setText("");
        txtKdMTK.requestFocus();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == txtKdMTK) {
            cariData();
        } else if (ae.getSource()==btnTambah){
            tambahData();
        } else if (ae.getSource() == btnBersih) {
            BERSIH();
        } else if (ae.getSource() == btnHapus) {
            hapusData();
        } else if (ae.getSource() == btnUbah) { // Penanganan aksi untuk tombol ubah
            ubahData();
        }
    }

    void cariData() {
        String idPegawai = txtKdMTK.getText().trim();
        if (!idPegawai.isEmpty()) {
            try {
                String query = "SELECT * FROM tblpegawai WHERE Id_Pegawai=?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, idPegawai);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    // Data ditemukan, mengisi field dengan data dari database
                    txtNMMTK.setText(result.getString("Nm_Pegawai"));
                    txtSKS.setText(result.getString("Alamat"));

                    // Menonaktifkan tombol tambah, mengaktifkan tombol ubah dan hapus
                    btnTambah.setEnabled(false);
                    btnUbah.setEnabled(true);
                    btnHapus.setEnabled(true);
                } else {
                    // Data tidak ditemukan
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan");

                    // Mengaktifkan tombol tambah, menonaktifkan tombol ubah dan hapus
                    btnTambah.setEnabled(true);
                    btnUbah.setEnabled(false);
                    btnHapus.setEnabled(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new frmPegawai();
    }
}
