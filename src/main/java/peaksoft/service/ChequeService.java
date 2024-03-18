package peaksoft.service;

import peaksoft.dto.request.ChequeRequest;
import peaksoft.dto.response.GetCheckResponse;
import peaksoft.dto.response.SimpleResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChequeService {
    SimpleResponse saveCheque(ChequeRequest chequeSaveRequest);

    SimpleResponse update(Long chequeId, ChequeRequest chequeRequest);

    SimpleResponse remove(Long chequeId);

    List<GetCheckResponse> findAllCheque();

    GetCheckResponse findChequeById(Long checkId);

    List<GetCheckResponse> getChequeByUserId(Long userId);

    String getChequeByUserIdAndDate(Long userId, LocalDate localDate);

    String avgRest();

    SimpleResponse deleteMenuOfCheck(Long checkId,ChequeRequest chequeRequest);
}
