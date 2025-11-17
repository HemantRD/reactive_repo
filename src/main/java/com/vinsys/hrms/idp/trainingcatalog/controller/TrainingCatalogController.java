package com.vinsys.hrms.idp.trainingcatalog.controller;

import com.vinsys.hrms.datamodel.HRMSBaseResponse;
import com.vinsys.hrms.exception.HRMSException;
import com.vinsys.hrms.idp.helper.ResponseGenerator;
import com.vinsys.hrms.idp.trainingcatalog.vo.SearchTopicReq;
import com.vinsys.hrms.idp.trainingcatalog.service.ITrainingCatalogService;
import com.vinsys.hrms.idp.trainingcatalog.vo.CompetencyDDLVo;
import com.vinsys.hrms.idp.trainingcatalog.vo.TrainingCatalogVo;
import com.vinsys.hrms.spring.BackendProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainingCatalog")
public class TrainingCatalogController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ITrainingCatalogService trainingCatalogService;
    private final BackendProperties props;

    public TrainingCatalogController(final ITrainingCatalogService trainingCatalogService,
                                     final BackendProperties props) {
        this.trainingCatalogService = trainingCatalogService;
        this.props = props;
    }

    @PostMapping
    HRMSBaseResponse<String> addUpdateTrainingCatalog(@RequestBody TrainingCatalogVo trainingCatalogVo) {
        HRMSBaseResponse<String> response;
        try {
            response = trainingCatalogService.addUpdateTrainingCatalog(trainingCatalogVo);
            response.setApplicationVersion(props.getApp_version());
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getValidationResponse(e, props.getApp_version());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getErrorResponse(e, props.getApp_version());
        }
        return response;
    }

    @GetMapping
    HRMSBaseResponse<?> getById(@RequestParam(value = "id") Long id) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.getById(id);
            response.setApplicationVersion(props.getApp_version());
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getValidationResponse(e, props.getApp_version());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getErrorResponse(e, props.getApp_version());
        }
        return response;
    }

    @GetMapping("list")
    HRMSBaseResponse<?> getAllTrainingCatalogs(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.getAllTrainingCatalogs(keyword, pageable);
            response.setApplicationVersion(props.getApp_version());
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getValidationResponse(e, props.getApp_version());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getErrorResponse(e, props.getApp_version());
        }
        return response;
    }

    @GetMapping("competencyTypes")
    HRMSBaseResponse<?> getCompetencyTypes() {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = trainingCatalogService.getCompetencyTypes();
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @GetMapping("competencySubTypes")
    HRMSBaseResponse<List<CompetencyDDLVo>> getCompetencySubTypes(@RequestParam(value = "id") Integer id) {
        HRMSBaseResponse<List<CompetencyDDLVo>> response = trainingCatalogService.getCompetencySubTypes(id);
        response.setApplicationVersion(props.getApp_version());
        return response;
    }

    @PostMapping(value = "searchTopics")
    HRMSBaseResponse<?> searchTopics(@RequestBody SearchTopicReq request,
                                     @RequestParam(required = false) String keyword,
                                     @PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
        HRMSBaseResponse<?> response;
        try {
            response = trainingCatalogService.searchTopics(request, keyword, pageable);
            response.setApplicationVersion(props.getApp_version());
        } catch (HRMSException e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getValidationResponse(e, props.getApp_version());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = ResponseGenerator.getErrorResponse(e, props.getApp_version());
        }
        return response;
    }


}
