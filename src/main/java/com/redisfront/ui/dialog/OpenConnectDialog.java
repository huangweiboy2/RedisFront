package com.redisfront.ui.dialog;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.redisfront.RedisFrontApplication;
import com.redisfront.model.ConnectInfo;
import com.redisfront.service.ConnectService;
import com.redisfront.ui.component.AbstractDialog;
import com.redisfront.model.ConnectTableModel;
import com.redisfront.util.MsgUtil;
import com.redisfront.util.Fn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.function.Consumer;

public class OpenConnectDialog extends AbstractDialog<ConnectInfo> {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable connectTable;
    private JButton addConnectBtn;

    protected Consumer<ConnectInfo> openActionCallback;

    protected Consumer<ConnectInfo> editActionCallback;

    protected Consumer<ConnectInfo> delActionCallback;

    public static void showOpenConnectDialog(Consumer<ConnectInfo> openActionCallback, Consumer<ConnectInfo> editActionCallback, Consumer<ConnectInfo> delActionCallback) {
        var openConnectDialog = new OpenConnectDialog(RedisFrontApplication.frame, openActionCallback, editActionCallback, delActionCallback);
        openConnectDialog.setSize(new Dimension(500, 280));
        openConnectDialog.setLocationRelativeTo(RedisFrontApplication.frame);
        openConnectDialog.pack();
        openConnectDialog.setVisible(true);
    }

    public OpenConnectDialog(Frame owner, Consumer<ConnectInfo> openActionCallback, Consumer<ConnectInfo> editActionCallback, Consumer<ConnectInfo> delActionCallback) {
        super(owner);
        $$$setupUI$$$();
        setTitle("打开连接");
        setModal(true);
        setMinimumSize(new Dimension(400, 500));
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        connectTableInit();
        this.openActionCallback = openActionCallback;
        this.editActionCallback = editActionCallback;
        this.delActionCallback = delActionCallback;
    }


    private void connectTableInit() {
        connectTable.setShowHorizontalLines(true);
        connectTable.setShowVerticalLines(true);
        connectTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        });
        connectTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        connectTable.setComponentPopupMenu(new JPopupMenu() {
            {
                //表格打开链接操作
                var openConnectMenu = new JMenuItem("打开链接");
                openConnectMenu.addActionListener(e -> {
                    var row = connectTable.getSelectedRow();
                    if (row == -1) {
                        MsgUtil.showInformationDialog("未选中打开行或列");
                        return;
                    }
                    var id = connectTable.getValueAt(row, 0);
                    var connectInfo = ConnectService.service.getConnect(id);
                    onCancel();
                    openActionCallback.accept(connectInfo);
                });
                add(openConnectMenu);
                //表格编辑链接操作
                var editConnectMenu = new JMenuItem("编辑链接");
                editConnectMenu.addActionListener(e -> {
                    var row = connectTable.getSelectedRow();
                    if (row == -1) {
                        MsgUtil.showInformationDialog("未选中编辑行或列");
                        return;
                    }
                    var id = connectTable.getValueAt(row, 0);
                    var connectInfo = ConnectService.service.getConnect(id);
                    onCancel();
                    editActionCallback.accept(connectInfo);
                });
                add(editConnectMenu);
                //表格删除操作
                var deleteConnectMenu = new JMenuItem("删除链接");
                deleteConnectMenu.addActionListener(e -> {
                    int row = connectTable.getSelectedRow();
                    if (row == -1) {
                        MsgUtil.showInformationDialog("未选中删除行或列");
                        return;
                    }
                    var id = connectTable.getValueAt(row, 0);
                    var connectInfo = ConnectService.service.getConnect(id);
                    if (Fn.isNotNull(connectInfo)) {
                        delActionCallback.accept(connectInfo);
                    }
                    ((ConnectTableModel) connectTable.getModel()).removeRow(row);
                    connectTable.revalidate();
                });
                add(deleteConnectMenu);
            }
        });
        //查询数据连接列表
        List<ConnectInfo> connectInfoList = ConnectService.service.getAllConnectList();
        connectTable.setModel(new ConnectTableModel(connectInfoList, "编号", "名称", "地址", "端口", "SSL", "连接模式"));
    }

    private void onOK() {
        var row = connectTable.getSelectedRow();
        if (row == -1) {
            MsgUtil.showInformationDialog("未选中编辑行或列");
            return;
        }
        var id = connectTable.getValueAt(row, 0);
        var connectInfo = ConnectService.service.getConnect(id);
        dispose();
        openActionCallback.accept(connectInfo);

    }

    private void onCancel() {
        dispose();
    }

    private void createUIComponents() {
        addConnectBtn = new JButton();
        addConnectBtn.addActionListener(e -> {
            dispose();
            AddConnectDialog.showAddConnectDialog(openActionCallback);
        });
        addConnectBtn.setIcon(new FlatSVGIcon("icons/connection.svg"));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("打开");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("取消");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, BorderLayout.CENTER);
        connectTable = new JTable();
        connectTable.setAutoResizeMode(1);
        scrollPane1.setViewportView(connectTable);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addConnectBtn.setText("添加新连接");
        panel4.add(addConnectBtn, BorderLayout.WEST);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}