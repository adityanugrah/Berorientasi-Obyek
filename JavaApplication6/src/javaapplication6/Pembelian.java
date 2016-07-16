/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;

/**
 *
 * @author Junior
 */
public final class Pembelian extends javax.swing.JFrame {

    /**
     * Creates new form Pembelian
     */
    public Pembelian() {
        initComponents();
        Date();
        autoNumber();
        namaSup();
        namaBar();
        listBarang();
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
    
    private void reset() {
        cbSupplier.setSelectedItem(null);
        cbBarang.setSelectedItem(null);
        txtBiaya.setText("");
        txtKapasitas.setText("");
        txtJumlah.setText("");
    }
    
    public void autoNumber() {
	try {
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String query = "SELECT COUNT(right(IDPEMBELIAN,1)) AS IDPEMBELIAN FROM pembelian";
            txtIDBeli.setEnabled(false);
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
		if(rs.first() == false){
                    txtIDBeli.setText("PM01");
                } else {
                    rs.last();
                    int noPegawai = rs.getInt(1) + 1;
                    String no = String.valueOf(noPegawai);
                    int noLong = no.length();
                    for(int a=0;a<2-noLong;a++) {
                        no = "0" + no;
                    }
                        txtIDBeli.setText("PM" + no);
                }
            }
                rs.close();
		statement.close();
	} catch(Exception ex) {
            System.out.println(ex);
	}
    }
    
    public void namaSup(){
        try { 
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String sql = "SELECT NAMASUPPLIER FROM `supplier`";
            ResultSet res = statement.executeQuery(sql);  
            while(res.next()){ 
                Object[] ob = new Object[3]; 
                ob[0] = res.getString(1); 
                cbSupplier.addItem((String) ob[0]); 
            } res.close(); 
            statement.close(); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
    }
    
    public void namaBar(){
        try { 
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            String sql = "SELECT NAMABRG FROM `barang`";
            ResultSet res = statement.executeQuery(sql);  
            while(res.next()){ 
                Object[] ob = new Object[3]; 
                ob[0] = res.getString(1); 
                cbBarang.addItem((String) ob[0]);
            } res.close(); 
            statement.close(); 
        } catch (Exception e) { 
            System.out.println(e.getMessage()); 
        }
    }
    
    public void listBarang(){
        try {
	Statement statement = (Statement) koneksi.GetConnection().createStatement();
	String sql = "SELECT HRGBELI,STOK FROM `barang` WHERE NAMABRG='"+cbBarang.getSelectedItem()+"'";
	ResultSet res = statement.executeQuery(sql); 
	while(res.next()){ 
		Object[] ob = new Object[3]; 
		ob[0]= res.getString(1); 
		ob[1]= res.getString(2);
		
		txtBiaya.setText((String) ob[0]); 
		txtKapasitas.setText((String) ob[1]); 
	} 
	res.close(); 
	statement.close(); 
        } catch (Exception e) { 
                System.out.println(e.getMessage()); 
        }
    }
    
    public void lihatdata() {
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("ID Pembelian");
        tbl.addColumn("Nama Supplier");
        tbl.addColumn("Nama Barang");
        tbl.addColumn("Jumlah");
        tbl.addColumn("Harga");
        tbl.addColumn("Total Harga");
        tableBeli.setModel(tbl);
        try {
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM `detailpembelian` WHERE IDPEMBELIAN = '"+txtIDBeli.getText()+"'");
            while (res.next()) {
                tbl.addRow(new Object[]{
                    res.getString("IDPEMBELIAN"),
                    res.getString("NAMASUPPLIER"),
                    res.getString("NAMABRG"),
                    res.getString("JUMLAH"),
                    res.getString("HARGA"),
                    res.getString("SUBHARGA")
                });
                tableBeli.setModel(tbl);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "salah");
        }
    }
    
    private void pTambah() {
        String ID      = txtIDBeli.getText();       
        //String tampilan ="yyyy-MM-dd HH:mm:ss" ;
        String tampilan ="yyyy-MM-dd" ;
        SimpleDateFormat fm = new SimpleDateFormat(tampilan); 
        String tanggal = String.valueOf(fm.format(tDate.getDate()));
                
        int TotalB = Integer.parseInt(lBelanja.getText());
        int Bayar  = Integer.parseInt(txtBayar.getText());
        int Kembali = Integer.parseInt(lKembali.getText());
        
        try {
            if (txtIDBeli.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "ID Pembelian Tidak Boleh Kosong");
            } else if (Bayar == 0) {
                JOptionPane.showMessageDialog(null, "Anda Belum Melakukan Pembayaran");
            } else if (Bayar < TotalB)  {
                JOptionPane.showMessageDialog(null, "Pembayaran Anda Tidak Memenuhi Transaksi");                
            } else {
                Statement statement = (Statement) koneksi.GetConnection().createStatement();
                statement.executeUpdate("INSERT INTO `bo`.`pembelian` VALUES ('"+ID+"', NULL, NULL, '"+tanggal+"', '"+TotalB+"');");
                statement.close();
                
                Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
                statement1.executeUpdate("UPDATE `bo`.`detailpembelian` SET `BAYAR` = '"+Bayar+"', `KEMBALI` = '"+Kembali+"' WHERE `detailpembelian`.`IDPEMBELIAN` = '"+ID+"';");
                statement1.close(); 
                JOptionPane.showMessageDialog(null, "Transaksi Berhasil Dilakukan");
                dispose();
                new Pembelian().setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Tersimpan, Tlg Cek Inputan Anda");
        }
    }
    
    private void dTambah() {
        String ID        = txtIDBeli.getText();       
        Object nSupplier = cbSupplier.getSelectedItem();
        int Jumlah       = Integer.parseInt(txtJumlah.getText());
        int Harga        = Integer.parseInt(txtBiaya.getText());
        
        if (nSupplier != null)
        {
            String selectedItemStr = nSupplier.toString();
        }
        
        Object nBarang = cbBarang.getSelectedItem();
        if (nBarang != null)
        {
            String selectedItemStr = nBarang.toString();
        }
            
        try {
            String sql1="Select * from detailpembelian where IDPEMBELIAN='" +ID +"' AND NAMABRG = '"+nBarang+"'";
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            ResultSet res = statement.executeQuery(sql1); 
            if (res.next()) {
                Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
                statement1.executeUpdate("UPDATE `bo`.`detailpembelian` SET `Jumlah` = Jumlah+ '"+Jumlah+"', `SUBHARGA` = Jumlah* '"+Harga+"' WHERE `detailpembelian`.`IDPEMBELIAN` = '"+ID+"' AND `NAMABRG` = '"+nBarang+"'");
                statement1.close();
                
                Statement statement2 = (Statement) koneksi.GetConnection().createStatement();
                statement2.executeUpdate("UPDATE `bo`.`barang` SET `STOK` = STOK+ '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';;");
                statement2.close();
            } else {
                Statement statement3 = (Statement) koneksi.GetConnection().createStatement();
                statement3.executeUpdate("INSERT INTO `bo`.`detailpembelian` VALUES (NULL, '"+ID+"', '"+nSupplier+"', '"+nBarang+"', '"+Jumlah+"', '"+Harga+"', '"+Jumlah+"'*'"+Harga+"', '0', '0');");
                statement3.close();
                
                Statement statement4 = (Statement) koneksi.GetConnection().createStatement();
                statement4.executeUpdate("UPDATE `bo`.`barang` SET `STOK` = STOK+ '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';;");
                statement4.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Tidak Tersimpan, Tlg Cek Inputan Anda");
        }
    }
    
    private void totalbayar () {
        String ID        = txtIDBeli.getText();
        if (!ID.equals("")) {
            String sql = "SELECT sum(SUBHARGA) FROM detailpembelian WHERE IDPEMBELIAN = '"+ID+"'";
            try {
                Statement statement = (Statement) koneksi.GetConnection().createStatement();
                ResultSet res = statement.executeQuery(sql);
                
                while (res.next()) {
                    lBelanja.setText(res.getString(1));
                }
                statement.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            } 
        } else {
            lBelanja.setText("");
        }
    }
    
    private void totalkembali () {
        String ID        = txtIDBeli.getText();
        int Bayar        = Integer.parseInt(txtBayar.getText());
        
        if (!ID.equals("")) {
            String sql = "SELECT ('"+Bayar+"' - SUM(SUBHARGA)) FROM detailpembelian WHERE IDPEMBELIAN = '"+ID+"'";
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIDBeli = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtBiaya = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tDate = new com.toedter.calendar.JDateChooser();
        cbSupplier = new javax.swing.JComboBox<>();
        cbBarang = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBeli = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtKapasitas = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lBelanja = new javax.swing.JLabel();
        lKembali = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 255, 0));
        jPanel1.setForeground(new java.awt.Color(255, 255, 0));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Pembelian.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("TRANSAKSI PEMBELIAN JUNIOR");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Semoga Belanja Anda Menyenangkan");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(323, 323, 323)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("ID Pembelian");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Nama Supplier");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Nama Barang");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Harga");

        txtBiaya.setMinimumSize(new java.awt.Dimension(6, 28));
        txtBiaya.setPreferredSize(new java.awt.Dimension(6, 28));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Jumlah");

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

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Tanggal Pembelian");

        cbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Supplier" }));
        cbSupplier.setPreferredSize(new java.awt.Dimension(90, 20));

        cbBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Barang" }));
        cbBarang.setMinimumSize(new java.awt.Dimension(94, 28));
        cbBarang.setPreferredSize(new java.awt.Dimension(94, 28));
        cbBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbBarangActionPerformed(evt);
            }
        });

        tableBeli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Pembelian", "Nama Supplier", "Nama Barang", "Jumlah", "Harga", "Total Harga"
            }
        ));
        tableBeli.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBeliMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBeli);

        btnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Add.png"))); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Menu.png"))); // NOI18N
        btnMenu.setText("Menu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tampilan/Save.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Kapasitas");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Total Pembayaran");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("Total Belanja");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 204));
        jLabel10.setText("Total Pengembalian");

        lBelanja.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lBelanja.setForeground(new java.awt.Color(255, 0, 0));
        lBelanja.setText("0");

        lKembali.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lKembali.setForeground(new java.awt.Color(0, 0, 204));
        lKembali.setText("0");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("Rp.");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Rp.");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 204));
        jLabel18.setText("Rp.");

        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtKapasitas, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtJumlah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                        .addComponent(txtBiaya, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(116, 116, 116)
                                .addComponent(btnTambah))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(tDate, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(72, 72, 72))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addGap(41, 41, 41))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(31, 31, 31)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16))
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lBelanja)
                                    .addComponent(lKembali)
                                    .addComponent(txtBayar))))
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnSimpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMenu)
                        .addGap(24, 24, 24))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtIDBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addComponent(tDate, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBiaya, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel17)
                                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(lBelanja)
                                .addComponent(jLabel16)))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(lKembali)
                            .addComponent(jLabel18))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnMenu)
                        .addComponent(btnSimpan))
                    .addComponent(btnTambah)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtKapasitas, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbBarangActionPerformed
        // TODO add your handling code here:
        listBarang();
    }//GEN-LAST:event_cbBarangActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        pTambah();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        dTambah();
        reset();
        totalbayar();
        lihatdata();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        new MenuUtama().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnMenuActionPerformed

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        // TODO add your handling code here:
       totalkembali();
    }//GEN-LAST:event_txtBayarKeyReleased

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        // TODO add your handling code here:
         
    }//GEN-LAST:event_txtBayarKeyPressed

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahActionPerformed

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void tableBeliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBeliMouseClicked
        // TODO add your handling code here:
        try {
            int row = tableBeli.getSelectedRow();
            String ID = tableBeli.getModel().getValueAt(row,0).toString();
            String nBarang = tableBeli.getModel().getValueAt(row,2).toString();
            String Jumlah = tableBeli.getModel().getValueAt(row,3).toString();
            
            Statement statement = (Statement) koneksi.GetConnection().createStatement();
            statement.executeUpdate("UPDATE `bo`.`barang` SET `STOK` =STOK- '"+Jumlah+"' WHERE `barang`.`NAMABRG` = '"+nBarang+"';");
            statement.close();
            
            Statement statement1 = (Statement) koneksi.GetConnection().createStatement();
            statement1.executeUpdate("Delete from detailpembelian where IDPEMBELIAN = '"+ID+"' AND NAMABRG = '"+nBarang+"'");
            statement1.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            lihatdata();
        }
    }//GEN-LAST:event_tableBeliMouseClicked

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarActionPerformed

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
            java.util.logging.Logger.getLogger(Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Pembelian().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cbBarang;
    private javax.swing.JComboBox<String> cbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel lBelanja;
    private javax.swing.JLabel lKembali;
    private com.toedter.calendar.JDateChooser tDate;
    private javax.swing.JTable tableBeli;
    private javax.swing.JTextField txtBayar;
    private javax.swing.JTextField txtBiaya;
    private javax.swing.JTextField txtIDBeli;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKapasitas;
    // End of variables declaration//GEN-END:variables
}
