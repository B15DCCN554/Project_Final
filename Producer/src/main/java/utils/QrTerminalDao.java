package utils;

import bean.QrTerminalPo;

import java.util.List;

public interface QrTerminalDao {
    List<QrTerminalPo> getAll();
    QrTerminalPo insertQrTerminal(QrTerminalPo qrTerminalPo);
    QrTerminalPo insertQrTerminalTest(QrTerminalPo qrTerminalPo);
}
