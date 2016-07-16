/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import koneksi.koneksi;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Junior
 */
public class Penjualan extends javax.swing.JFrame {

    /**
     * Creates new form Penjualan
     */
    public Penjualan() {
        initComponents();
        Date();
        autoNumber();
        namakar();
        namabar();
        barang();
        lihatdata();
        totalbayar();
        txtJumlah.setText("0");
        txtBayar.setText("0");
    }
    
    public void Date() {
        Date tgl = new Date();
        tDate.setDate(tgl);
        tDate.setEnabled(false);
    }
    
    private void reset1() {
        cbNamaBrg.setSelectedItem(null);
        txtHarga.setText("");
        txtStok.setText("");
        txtJumlah.setText("");
    }
    
     private void reset2() {
        lTotal.setText(null);
        txtBayar.setText("");
        lKembali.setText(null);
    }
    
    public void lihatdata() {
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("ID Penjualan");
        tbl.addColumn("Nama Karyawan");
        tbl.addColumn("Nama Barang");
        tbl.addColumn("Jumlah");
        tbl.addColumn("Harga");
        tbl.addColumn("Total Harga");
        tableJual.setModel(tbl);
        try {
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM `detailpenjualan` WHERE IDPENJUALAN = '"+txtIDJual.getText()+"'");
            while (res.next()) {
                tbl.addRow(new Object[]{
                    res.getString("IDPENJUALAN"),
                    res.getString("NAMAKARYAWAN"),
                    res.getString("NAMABRG"),
                    res.getString("sJumlah"),
                    res.getString("HARGA"),
                    res.getString("SUBHARGA")
                });
                tableJual.setModel(tbl);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "salah");
        }
    }
        
    public void autoNumber() {
	try {
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String query = "SELECT COUNT(right(IDPENJUALAN,1)) AS no FROM penjualan";
            txtIDJual.setEnabled(false);
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
		if(rs.first() == false){
                    txtIDJual.setText("PJ01");
                } else {
                    rs.last();
                    int noPegawai = rs.getInt(1) + 1;
                    String no = String.valueOf(noPegawai);
                    int noLong = no.length();
                    for(int a=0;a<2-noLong;a++) {
                        no = "0" + no;
                    }
                        txtIDJual.setText("PJ" + no);
                }
            }
                rs.close();
		statement.close();
	} catch(Exception ex) {
            System.out.println(ex);
	}
    }
    
    public void namakar(){
        try { 
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String sql = "SELECT NAMAKARYAWAN FROM `karyawan`";
            ResultSet res = statement.executeQuery(sql);  
            while(res.next()){ 
                Object[] ob = new Object[3]; 
                ob[0] = res.getString(1); 
                cbNamaKar.addItem((String) ob[0]); 
            } res.close(); 
            statement.close(); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
    }
    
    public void namabar(){
        try { 
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String sql = "SELECT NAMABRG FROM `barang`";
            ResultSet res = statement.executeQuery(sql);  
            while(res.next()){ 
                Object[] ob = new Object[3]; 
                ob[0] = res.getString(1); 
                cbNamaBrg.addItem((String) ob[0]);
            } res.close(); 
            statement.close(); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
    }
    
    public void barang(){
        try {
	Statement statement = (Statement) koneksi.GetConnection().createStatement();
	String sql = "SELECT HARGAJUAL,STOK FROM `barang` WHERE NAMABRG='"+cbNamaBrg.getSelectedItem()+"'";
	ResultSet res = statement.executeQuery(sql); 
	while(res.next()){ 
		Object[] ob = new Object[3]; 
		ob[0]= res.getString(1); 
		ob[1]= res.getString(2);
		
		txtHarga.setText((String) ob[0]); 
		txtStok.setText((String) ob[1]); 
	} 
	res.close(); 
	statement.close(); 
        } catch (Exception e) { 
                System.out.println(e.getMessage()); 
        }
    }
    
    private void jTambah() {
        String ID       = txtIDJual.getText();     
        String tampilan ="yyyy-MM-dd" ;
        SimpleDateFormat fm = new SimpleDateFormat(tampilan); 
        String tanggal = String.valueOf(fm.format(tDate.getDate()));
        
        int TotalB = Integer.parseInt(lTotal.getText());
        int Bayar  = Integer.parseInt(txtBayar.getText());
        int Kembali = Integer.parseInt(lKembali.getText());
        
        try {
            if (txtIDJual.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "ID Penjualan Tidak Boleh Kosong");
            } else if (Bayar == 0) {
                JOptionPane.showMessageDialog(null, "Anda Belum Melakukan Pembayaran");
            } else if (Bayar < TotalB) {
                JOptionPane.showMessageDialog(null, "Pembayaran Anda Tidak Memenuhi Transaksi");
            } else {
                Statement statement = (Statement) koneksi.GetConnection().createStatement();
                statement.executeUpdate("INSERT INTO `bo`.`penjualan` VALUES ('"+ID+"', NULL, NULL, '"+tanggal+"', '"+TotalB+"');");
                statement.close();
                
                Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
                statement1.executeUpdate("UPDATE `bo`.`detailpenjualan` SET `BAYAR` = '"+Bayar+"', `KEMBALI` = '"+Kembali+"' WHERE `detailpenjualan`.`IDPENJUALAN` = '"+ID+"';");
                statement1.close(); 
                JOptionPane.showMessageDialog(null, "Transaksi Berhasil Dilakukan");
                reset2();
                dispose();
                new Penjualan().setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Transaksi Gagal, Segera Lakukan Pembayaran");
        }
    }
    
    private void dTambah() {
        String ID        = txtIDJual.getText();       
        Object nKaryawan = cbNamaKar.getSelectedItem();
        int Jumlah       = Integer.parseInt(txtJumlah.getText());
        int Harga        = Integer.parseInt(txtHarga.getText());
        
        if (nKaryawan != null)
        {
            String selectedItemStr = nKaryawan.toString();
        }
        
        Object nBarang = cbNamaBrg.getSelectedItem();
        if (nBarang != null)
        {
            String selectedItemStr = nBarang.toString();
        }
            
        try {
            String sql1="Select * from detailpenjualan where IDPENJUALAN='" +ID +"' AND NAMABRG = '"+nBarang+"'";
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            ResultSet res = statement.executeQuery(sql1); 
            if (res.next()) {
                Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
                statement1.executeUpdate("UPDATE `bo`.`detailpenjualan` SET `sJumlah` = sJumlah+ '"+Jumlah+"', `SUBHARGA` = sJumlah* '"+Harga+"' WHERE `detailpenjualan`.`IDPENJUALAN` = '"+ID+"' AND `NAMABRG` = '"+nBarang+"'");
                statement1.close();
                
                Statement statement2 = (Statement) koneksi.GetConnection().createStatement();
                statement2.executeUpdate("UPDATE `bo`.`barang` SET `STOK` = STOK- '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';;");
                statement2.close();
            } else {
                Statement statement3 = (Statement) koneksi.GetConnection().createStatement();
                statement3.executeUpdate("INSERT INTO `bo`.`detailpenjualan` VALUES (NULL, '"+ID+"', '"+nKaryawan+"', '"+nBarang+"', '"+Jumlah+"', '"+Harga+"', '"+Jumlah+"'*'"+Harga+"', '0', '0');");
                statement3.close();
                
                Statement statement4 = (Statement) koneksi.GetConnection().createStatement();
                statement4.executeUpdate("UPDATE `bo`.`barang` SET `STOK` = STOK- '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';;");
                statement4.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Tersimpan, Tlg Cek Inputan Anda");
        }
    }
    
    private void totalbayar () {
        String ID        = txtIDJual.getText();
        if (!ID.equals("")) {
            String sql = "SELECT sum(SUBHARGA) FROM detailpenjualan WHERE IDPENJUALAN = '"+ID+"'";
            try {
                Statement statement = (Statement) koneksi.GetConnection().createStatement();
                ResultSet res = statement.executeQuery(sql);
                
                while (res.next()) {
                    lTotal.setText(res.getString(1));
                }
                statement.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
        } else {
            lTotal.setText("");
        }
    }
    
    private void totalkembali () {
        String ID        = txtIDJual.getText();
        int Bayar        = Integer.parseInt(txtBayar.getText());
        
        if (!ID.equals("")) {
            String sql = "SELECT ('"+Bayar+"' - SUM(SUBHARGA)) FROM detailpenjualan WHERE IDPENJUALAN = '"+ID+"'";
            try {
                Statement statement = (Statement) koneksi.GetConnection().createStatement();
                ResultSet res = statement.executeQuery(sql);
                
                while (res.next()) {
                    lKembali.setText(res.getString(1));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
        } else {
            lKembali.setText("");
        }
    }
    
    public void kapasitas () {
        int Jumlah =Integer.parseInt(txtJumlah.getText());
        int Stok =Integer.parseInt(txtStok.getText());
        try { 
            if (Jumlah <= Stok) {
                String hJumlah = txtJumlah.getText();       
                String hKapasitas = txtStok.getText();
            } else{
                txtJumlah.setText("");
                JOptionPane.showMessageDialog(null, "Kapasitas Tidak Tersedia");
            }
        } catch (Exception e) {
            txtJumlah.setText("");
            JOptionPane.showMessageDialog(null, "Kapasitas Tidak Tersedia");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtIDJual = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtHarga = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtStok = new javax.swing.JTextField();
        cbNamaBrg = new javax.swing.JComboBox<>();
        btnTambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableJual = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbNamaKar = new javax.swing.JComboBox<>();
        tDate = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        lTotal = new javax.swing.JLabel();
        lKembali = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();
        rp = new javax.swing.JLabel();
        rp1 = new javax.swing.JLabel();
        btnMenu = new javax.swing.JButton();
        rp2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 153, 0));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("TRANSAKSI PENJUALAN JUNIOR");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Semoga Belanja Anda Menyenangkan");

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Penjualan.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(295, 295, 295)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addContainerGap(390, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("ID Penjualan");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Tanggal Penjualan");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Nama Barang");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Harga");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Jumlah");

        cbNamaBrg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Barang" }));
        cbNamaBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNamaBrgActionPerformed(evt);
            }
        });

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Add.png"))); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        tableJual.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Penjualan", "Nama Karyawan", "Nama Barang", "Jumlah", "Harga", "Total Harga"
            }
        ));
        tableJual.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableJualMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableJual);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Total Pembayaran");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Total Pengembalian");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Kapasitas");

        txtJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahActionPerformed(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Nama Karyawan");

        cbNamaKar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Karyawan" }));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Total Belanja");

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Save.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        lTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lTotal.setForeground(new java.awt.Color(255, 0, 0));
        lTotal.setText("0");

        lKembali.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lKembali.setForeground(new java.awt.Color(102, 0, 153));
        lKembali.setText("0");

        txtBayar.setText("0");
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
        });

        rp.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        rp.setForeground(new java.awt.Color(102, 0, 153));
        rp.setText("Rp.");

        rp1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        rp1.setForeground(new java.awt.Color(102, 102, 102));
        rp1.setText("Rp.");

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Menu.png"))); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        rp2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        rp2.setForeground(new java.awt.Color(255, 0, 0));
        rp2.setText("Rp.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDJual, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNamaKar, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(105, 105, 105)
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(tDate, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(btnTambah)
                        .addGap(10, 10, 10)
                        .addComponent(btnMenu)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rp2)
                                .addGap(18, 18, 18)
                                .addComponent(lTotal))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rp1)
                                .addGap(18, 18, 18)
                                .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(134, 134, 134))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rp)
                        .addGap(18, 18, 18)
                        .addComponent(lKembali)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSimpan)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtIDJual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addComponent(tDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNamaKar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(96, 96, 96)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnMenu)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(lTotal)
                    .addComponent(rp2))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(rp1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lKembali)
                            .addComponent(rp))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(btnSimpan)
                        .addGap(26, 26, 26))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbNamaBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNamaBrgActionPerformed
        // TODO add your handling code here:
        barang();
    }//GEN-LAST:event_cbNamaBrgActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        dTambah();
        totalbayar();
        reset1();
        lihatdata();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        jTambah();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        // TODO add your handling code here:
        totalkembali();
    }//GEN-LAST:event_txtBayarKeyPressed

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        // TODO add your handling code here:        
        totalkembali();
    }//GEN-LAST:event_txtBayarKeyReleased

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnMenuActionPerformed

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahActionPerformed

    private void tableJualMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableJualMouseClicked
        // TODO add your handling code here:
        try {
            int row = tableJual.getSelectedRow();
            String ID = tableJual.getModel().getValueAt(row,0).toString();
            String nBarang = tableJual.getModel().getValueAt(row,2).toString();
            String Jumlah = tableJual.getModel().getValueAt(row,3).toString();
            
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            statement.executeUpdate("UPDATE `bo`.`barang` SET `STOK` =STOK+ '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';");
            statement.close();
            
            Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
            statement1.executeUpdate("Delete from detailpenjualan where IDPENJUALAN = '"+ID+"' AND NAMABRG = '"+nBarang+"'");
            statement1.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            lihatdata();
        }        
    }//GEN-LAST:event_tableJualMouseClicked

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
        kapasitas();
    }//GEN-LAST:event_txtJumlahKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Penjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Penjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cbNamaBrg;
    private javax.swing.JComboBox<String> cbNamaKar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lKembali;
    private javax.swing.JLabel lTotal;
    private javax.swing.JLabel rp;
    private javax.swing.JLabel rp1;
    private javax.swing.JLabel rp2;
    private com.toedter.calendar.JDateChooser tDate;
    private javax.swing.JTable tableJual;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtIDJual;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
