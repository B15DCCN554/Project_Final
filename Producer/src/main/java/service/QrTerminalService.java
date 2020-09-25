package service;

import bean.QrTerminalPo;
import common.CommonPool;
import common.JsonCustom;
import oracle.jdbc.OracleTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.QrTerminalDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QrTerminalService implements QrTerminalDao {
    private static final Logger LOG = LogManager.getLogger(QrTerminalService.class);
    private static final String GET_QR_TERMINAL = "{CALL PKG_QR_TERMINAL.PROC_GET_QR_TERMINAL(?)}";
    private static final String INSERT_QR_TERMINAL = "{CALL PKG_QR_TERMINAL.PROC_INSERT_QR_TERMINAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    private static final String INSERT_QR_TERMINAL_TEST = "{CALL PKG_QR_TERMINAL.PROC_INSERT_QR_TERMINAL_TEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    //get data qr_terminal
    public List<QrTerminalPo> getAll() {
        List<QrTerminalPo> listQrTerminal = new ArrayList<>();
        try (Connection connection = CommonPool.hikariDataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(GET_QR_TERMINAL)) {
            LOG.info("Begin get all data qr_terminal");
            stmt.registerOutParameter("P_CUR", OracleTypes.CURSOR);
            stmt.execute();
            try (ResultSet rs = (ResultSet) stmt.getObject("P_CUR")) {
                while (rs.next()) {
                    QrTerminalPo qrTerminalPo = new QrTerminalPo();
                    qrTerminalPo.setId(rs.getBigDecimal("ID"));
                    qrTerminalPo.setMasterMerchantName(rs.getString("MASTER_MERCHANT_NAME"));
                    qrTerminalPo.setMasterMerchant(rs.getString("MASTER_MERCHANT"));
                    qrTerminalPo.setCreateDate(rs.getTimestamp("CREATE_DATE"));
                    qrTerminalPo.setMccCode(rs.getString("TID_MCC_CODE"));
                    qrTerminalPo.setMccName(rs.getString("TID_MCC_NAME"));
                    qrTerminalPo.setMerchantCode(rs.getString("MERCHANT_CODE"));
                    qrTerminalPo.setMerchantId(rs.getBigDecimal("MERCHANT_ID"));
                    qrTerminalPo.setMerchantName(rs.getString("MERCHANT_NAME"));
                    qrTerminalPo.setTerminalId(rs.getString("TERMINAL_ID"));
                    qrTerminalPo.setTerminalName(rs.getString("TERMINAL_NAME"));
                    qrTerminalPo.setTidMccCode(rs.getString("TID_MCC_CODE"));
                    qrTerminalPo.setTidMccName(rs.getString("TID_MCC_NAME"));
                    qrTerminalPo.setTidMccVnpayCode(rs.getString("TID_MCC_VNPAY_CODE"));
                    qrTerminalPo.setTidMccVnpayName(rs.getString("TID_MCC_VNPAY_NAME"));
                    listQrTerminal.add(qrTerminalPo);
                    LOG.info(JsonCustom.convertObjectToJson(qrTerminalPo));
                }
            }
            LOG.info("End get all data qr_terminal");
        } catch (Exception e) {
            LOG.error("Error get data api get_qr_terminal: " + e.getMessage());
        }
        return listQrTerminal;
    }

    //insert data into qr_terminal
    public QrTerminalPo insertQrTerminal(QrTerminalPo qrTerminalPo) {
        try (Connection connection = CommonPool.hikariDataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(INSERT_QR_TERMINAL)) {
            LOG.info("Begin addQrTerminal with data: " + JsonCustom.convertObjectToJson(qrTerminalPo));
            stmt.setString("P_MASTER_MERCHANT", qrTerminalPo.getMasterMerchant());
            stmt.setString("P_MASTER_MERCHANT_NAME", qrTerminalPo.getMasterMerchantName());
            stmt.setString("P_MERCHANT_CODE", qrTerminalPo.getMerchantCode());
            stmt.setString("P_MERCHANT_NAME", qrTerminalPo.getMerchantName());
            stmt.setString("P_MCC_CODE", qrTerminalPo.getMccCode());
            stmt.setString("P_MCC_NAME", qrTerminalPo.getMccName());
            stmt.setString("P_TERMINAL_ID", qrTerminalPo.getTerminalId());
            stmt.setString("P_TERMINAL_NAME", qrTerminalPo.getTerminalName());
            stmt.setString("P_TID_MCC_CODE", qrTerminalPo.getTidMccCode());
            stmt.setString("P_TID_MCC_NAME", qrTerminalPo.getTidMccName());
            stmt.setString("P_TID_MCC_VNPAY_CODE", qrTerminalPo.getTidMccVnpayCode());
            stmt.setString("P_TID_MCC_VNPAY_NAME", qrTerminalPo.getTidMccVnpayName());
            stmt.setBigDecimal("P_MERCHANT_ID", qrTerminalPo.getMerchantId());
            stmt.registerOutParameter("P_CREATE_DATE", OracleTypes.TIMESTAMP);
            stmt.registerOutParameter("P_TRAN_ID", OracleTypes.INTEGER);
            stmt.execute();
            qrTerminalPo.setId(stmt.getBigDecimal("P_TRAN_ID"));
            qrTerminalPo.setCreateDate(stmt.getTimestamp("P_CREATE_DATE"));
            LOG.info("End addQrTerminal");
        } catch (Exception e) {
            LOG.error("Error insert qr_terminal: " + e.getMessage());
        }
        return qrTerminalPo;
    }

    //insert data into qr_terminal_test
    public QrTerminalPo insertQrTerminalTest(QrTerminalPo qrTerminalPo) {
        try (Connection connection = CommonPool.hikariDataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(INSERT_QR_TERMINAL_TEST)) {
            LOG.info("Begin addQrTerminalTest with data: " + JsonCustom.convertObjectToJson(qrTerminalPo));
            stmt.registerOutParameter("P_MASTER_MERCHANT", OracleTypes.VARCHAR);
            stmt.setString("P_MASTER_MERCHANT_NAME", qrTerminalPo.getMasterMerchantName());
            stmt.setString("P_MERCHANT_CODE", qrTerminalPo.getMerchantCode());
            stmt.setString("P_MERCHANT_NAME", qrTerminalPo.getMerchantName());
            stmt.setString("P_MCC_CODE", qrTerminalPo.getMccCode());
            stmt.setString("P_MCC_NAME", qrTerminalPo.getMccName());
            stmt.setString("P_TERMINAL_ID", qrTerminalPo.getTerminalId());
            stmt.setString("P_TERMINAL_NAME", qrTerminalPo.getTerminalName());
            stmt.setString("P_TID_MCC_CODE", qrTerminalPo.getTidMccCode());
            stmt.setString("P_TID_MCC_NAME", qrTerminalPo.getTidMccName());
            stmt.setString("P_TID_MCC_VNPAY_CODE", qrTerminalPo.getTidMccVnpayCode());
            stmt.setString("P_TID_MCC_VNPAY_NAME", qrTerminalPo.getTidMccVnpayName());
            stmt.setBigDecimal("P_MERCHANT_ID", qrTerminalPo.getMerchantId());
            stmt.registerOutParameter("P_CREATE_DATE", OracleTypes.TIMESTAMP);
            stmt.registerOutParameter("P_TRAN_ID", OracleTypes.INTEGER);
            stmt.execute();
            qrTerminalPo.setId(stmt.getBigDecimal("P_TRAN_ID"));
            qrTerminalPo.setCreateDate(stmt.getTimestamp("P_CREATE_DATE"));
            qrTerminalPo.setMasterMerchant(stmt.getString("P_MASTER_MERCHANT"));
            LOG.info("End addQrTerminalTest");
        } catch (Exception e) {
            LOG.error("Error insert qr_terminal_test: " + e.getMessage());
            //e.printStackTrace();

        }
        return qrTerminalPo;
    }
}
