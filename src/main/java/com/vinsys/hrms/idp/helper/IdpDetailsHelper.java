package com.vinsys.hrms.idp.helper;

import com.vinsys.hrms.dao.idp.ApprovedTrainingsDAO;
import com.vinsys.hrms.dao.idp.TrainingCatalogDAO;
import com.vinsys.hrms.entity.idp.ApprovedTrainings;
import com.vinsys.hrms.entity.idp.TrainingCatalog;
import com.vinsys.hrms.idp.dao.IIdpDetailCommentDAO;
import com.vinsys.hrms.idp.dao.IIdpDetailsDAO;
import com.vinsys.hrms.idp.dao.IIdpFlowHistoryDAO;
import com.vinsys.hrms.idp.entity.Idp;
import com.vinsys.hrms.idp.entity.IdpDetailComment;
import com.vinsys.hrms.idp.entity.IdpDetails;
import com.vinsys.hrms.idp.entity.IdpFlowHistory;
import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryActionType;
import com.vinsys.hrms.idp.enumconstant.IdpFlowHistoryStatus;
import com.vinsys.hrms.idp.enumconstant.IdpRecordStatus;
import com.vinsys.hrms.idp.enumconstant.IdpStatus;
import com.vinsys.hrms.idp.vo.IdpRequestVO;
import com.vinsys.hrms.idp.vo.getidp.IdpDetailVO;
import com.vinsys.hrms.idp.vo.submit.IdpSubmitVO;
import com.vinsys.hrms.master.dao.IMasterYearDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Helper class for IDP details operations (save, update, delete)
 */
@Component
public class IdpDetailsHelper {

    @Autowired
    IIdpDetailsDAO idpDetailsDAO;

    @Autowired
    IIdpDetailCommentDAO idpDetailCommentDAO;

    @Autowired
    IIdpFlowHistoryDAO idpFlowHistoryDAO;

    @Autowired
    ApprovedTrainingsDAO approvedTrainingsDAO;

    @Autowired
    IMasterYearDAO masterYearDAO;

    @Autowired
    TrainingCatalogDAO trainingCatalogDAO;

    /**
     * Save IDP details with comments
     */
    public void saveIdpDetails(Idp idp, List<IdpRequestVO.IdpDetailRequestVO> detailVOs) {
        // Convert VOs to entities
        List<IdpDetails> detailsList = detailVOs.stream()
                .map(vo -> createIdpDetail(idp, vo))
                .collect(Collectors.toList());

        // Save all details
        List<IdpDetails> savedDetails = idpDetailsDAO.saveAll(detailsList);

        // Save comments if provided and create initial progress tracking
        saveComments(idp, savedDetails, detailVOs);
    }

    /**
     * Create IdpDetails entity from VO
     */
    private IdpDetails createIdpDetail(Idp idp, IdpRequestVO.IdpDetailRequestVO vo) {
        IdpDetails detail = new IdpDetails();
        detail.setIdp(idp);
        detail.setCompetencyTypeId(vo.getCompetencyTypeId());
        detail.setCompetencySubTypeId(vo.getCompetencySubTypeId());
        detail.setDevGoals(vo.getDevGoal());
        detail.setDevActions(vo.getDevActions());

        // Set training reference if training_id is provided
        if (vo.getTrainingId() != null) {
            // Fetch TrainingCatalog from database and set it
            TrainingCatalog training = trainingCatalogDAO.findById(vo.getTrainingId()).orElse(null);
            if (training != null) {
                detail.setTraining(training);
            }
        }

        detail.setTrainingName(vo.getTrainingName());
        detail.setStatus(IdpStatus.DRAFT.getValue());
        detail.setPriority(vo.getPriority());
        return detail;
    }

    /**
     * Save comments for IDP details
     */
    private void saveComments(Idp idp, List<IdpDetails> savedDetails,
                              List<IdpRequestVO.IdpDetailRequestVO> detailVOs) {
        for (int i = 0; i < savedDetails.size(); i++) {
            IdpDetails detail = savedDetails.get(i);
            IdpRequestVO.IdpDetailRequestVO vo = detailVOs.get(i);

            if (vo.getComment() != null && !vo.getComment().trim().isEmpty()) {
                saveComment(idp, detail, vo.getComment());
            }
        }
    }

    /**
     * Save a single comment
     */
    public void saveComment(Idp idp, IdpDetails detail, String commentText) {
        IdpDetailComment comment = IdpDetailComment.builder()
                .idpId(idp.getId())
                .idpDetailId(detail.getId())
                .idpFlowId(idp.getActiveFlowId())
                .comment(commentText)
                .build();
        idpDetailCommentDAO.save(comment);
    }

    /**
     * Update IDP detail training information
     */
    public void updateDetailTraining(IdpDetails detail, IdpDetailVO detailVO) {
        // Update training entity reference if training_id is provided
        if (detailVO.getTrainingId() != null) {
            // Fetch TrainingCatalog from database if provided
            TrainingCatalog training = trainingCatalogDAO.findById(detailVO.getTrainingId()).orElse(null);
            if (training != null) {
                detail.setTraining(training);
            }
        } else {
            // Clear training if trainingId is null
            detail.setTraining(null);
        }

        if (detailVO.getTrainingName() != null) {
            detail.setTrainingName(detailVO.getTrainingName());
        }
        if (detailVO.getDevActions() != null) {
            detail.setDevActions(detailVO.getDevActions());
        }
        if (detailVO.getDevGoal() != null) {
            detail.setDevGoals(detailVO.getDevGoal());
        }
        if (detailVO.getCompetencyTypeId() != null) {
            detail.setCompetencyTypeId(detailVO.getCompetencyTypeId());
        }
        if (detailVO.getCompetencySubTypeId() != null) {
            detail.setCompetencySubTypeId(detailVO.getCompetencySubTypeId());
        }
        if (detailVO.getPriority() != null) {
            detail.setPriority(detailVO.getPriority());
        }
        idpDetailsDAO.save(detail);
    }

    /**
     * Create flow history entry
     */
    public void createFlowHistory(Idp idp, Long employeeId, String role) {
        IdpFlowHistory flowHistory = IdpFlowHistory.builder()
                .idp(idp)
                .employeeId(employeeId)
                .employeeRole(role)
                .startDate(Instant.now())
                .actionType(IdpFlowHistoryStatus.PENDING.getValue())
                .actionDate(null)
                .status(IdpFlowHistoryStatus.PENDING.getValue())
                .recordStatus(IdpRecordStatus.ACTIVE.getValue())
                .build();

        IdpFlowHistory savedFlow = idpFlowHistoryDAO.save(flowHistory);

        // Update IDP with active flow ID
        idp.setActiveFlowId(savedFlow.getId());
    }

    /**
     * update flow history entry
     */
    public void updateFlowHistory(IdpSubmitVO idpSubmitVO) {
        idpFlowHistoryDAO.findById(idpSubmitVO.getActiveFlowId())
                .ifPresent(history -> {
                    IdpFlowHistoryActionType actionType =
                            IdpFlowHistoryActionType.valueOf(idpSubmitVO.getAction().toUpperCase());
                    IdpFlowHistoryStatus status =
                            IdpFlowHistoryMapper.getStatus(actionType);
                    history.setActionType(actionType.getValue());
                    history.setActionDate(Instant.now());
                    history.setStatus(status.getValue());
                    history.setRecordStatus(IdpRecordStatus.INACTIVE.getValue());
                    idpFlowHistoryDAO.save(history);
                });
    }

    public Optional<IdpFlowHistory> getFlowHistoryById(Long flowId) {
       // Current Role fetch
        return idpFlowHistoryDAO.findById(flowId);
    }

    /**
     * Update all IDP details status to a specific status
     */
    public void updateIdpDetailsStatus(Idp idp, String newStatus) {
        List<IdpDetails> details = idpDetailsDAO.findAllByIdpId(idp.getId());
        if (details != null && !details.isEmpty()) {
            for (IdpDetails detail : details) {
                detail.setStatus(newStatus);
                idpDetailsDAO.save(detail);
            }
        }
    }

    /**
     * Save IDP details from old VO format (for backward compatibility with IdpController)
     */
    public void saveIdpDetailsFromVO(Idp idp, List<IdpDetailVO> detailVOs) {
        if (detailVOs == null || detailVOs.isEmpty()) {
            return;
        }

        // ✅ Step 1: Determine incoming competencyTypeIds from REQUEST
        List<Long> incomingTypeIds = detailVOs.stream()
                .map(IdpDetailVO::getCompetencyTypeId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // ✅ Step 2: Remove only matching OLD details (leave others intact)
        if (idp.getDetails() != null && !idp.getDetails().isEmpty()) {
            idp.getDetails().removeIf(old ->
                    old.getCompetencyTypeId() != null &&
                            incomingTypeIds.contains(old.getCompetencyTypeId())
            );
        }

        // Step 3: Convert old VO to new format
        List<IdpRequestVO.IdpDetailRequestVO> newFormatDetails = detailVOs.stream()
                .map(idpDetailVo -> IdpRequestVO.IdpDetailRequestVO.builder()
                        .id(idpDetailVo.getId())
                        .competencyTypeId(idpDetailVo.getCompetencyTypeId())
                        .competencySubTypeId(idpDetailVo.getCompetencySubTypeId())
                        .devGoal(idpDetailVo.getDevGoal())
                        .devActions(idpDetailVo.getDevActions())
                        .trainingId(idpDetailVo.getTrainingId())
                        .trainingName(idpDetailVo.getTrainingName())
                        .comment(idpDetailVo.getComment())
                        .priority(idpDetailVo.getPriority())
                        .build())
                .collect(Collectors.toList());

        // Step 4: Save using main method
        saveIdpDetails(idp, newFormatDetails);
    }

    /**
     * Update details with comments from old VO format (for backward compatibility with IdpController)
     */
    public void updateDetailsWithComments(Idp idp, List<IdpDetailVO> detailVOs, Long activeFlowId) {
        if (detailVOs == null || detailVOs.isEmpty()) {
            return;
        }

        for (IdpDetailVO detailVO : detailVOs) {
            if (detailVO.getId() != null) {
                // Find existing detail
                IdpDetails detail = idpDetailsDAO.findById(detailVO.getId())
                        .orElseThrow(() -> new RuntimeException("IDP Detail not found with id: " + detailVO.getId()));

                // Update training information if provided
                updateDetailTraining(detail, detailVO);

                // Add manager's comment/correction in separate comments table
                IdpDetailComment existing =
                        idpDetailCommentDAO.findByIdpIdAndIdpDetailIdAndIdpFlowId(
                                idp.getId(),
                                detail.getId(),
                                activeFlowId
                        );
                if (existing != null) {
                    existing.setComment(detailVO.getComment());
                    idpDetailCommentDAO.save(existing); // UPDATE
                } else {
                    IdpDetailComment newComment = IdpDetailComment.builder()
                            .idpId(idp.getId())
                            .idpDetailId(detail.getId())
                            .idpFlowId(activeFlowId)
                            .comment(detailVO.getComment())
                            .build();
                    idpDetailCommentDAO.save(newComment); // INSERT
                }
            }
        }
        List<IdpRequestVO.IdpDetailRequestVO> newFormatDetails = detailVOs.stream()
                .filter(idpDetailVo -> idpDetailVo.getId() == null)  // <-- Only map when detailId is null
                .map(idpDetailVo -> IdpRequestVO.IdpDetailRequestVO.builder()
                        .id(idpDetailVo.getId())
                        .competencyTypeId(idpDetailVo.getCompetencyTypeId())
                        .competencySubTypeId(idpDetailVo.getCompetencySubTypeId())
                        .devGoal(idpDetailVo.getDevGoal())
                        .devActions(idpDetailVo.getDevActions())
                        .trainingId(idpDetailVo.getTrainingId())
                        .trainingName(idpDetailVo.getTrainingName())
                        .comment(idpDetailVo.getComment())
                        .priority(idpDetailVo.getPriority())
                        .build())
                .collect(Collectors.toList());

        saveIdpDetails(idp, newFormatDetails);

    }

    /**
     * Create ApprovedTrainings records for ALL IDP details
     * This is called when TDHEAD approves the IDP
     * Creates one record per IDP detail (including non-training competencies)
     *
     * @param idp the approved IDP
     * @param employeeId the employee ID for whom the training is approved
     */
        public void createApprovedTrainingsForIdp(Idp idp, Long employeeId) {
            // Get all IDP details for this IDP
            List<IdpDetails> idpDetails = idpDetailsDAO.findAllByIdpId(idp.getId());

            if (idpDetails == null || idpDetails.isEmpty()) {
                return;
            }

            // Get flow history for the approval
            Optional<IdpFlowHistory> flowHistory = idpFlowHistoryDAO.findById(idp.getActiveFlowId());
            if (!flowHistory.isPresent()) {
                return;
            }

            IdpFlowHistory flow = flowHistory.get();

            // Create ApprovedTrainings for EVERY IDP detail (including non-training items)
            for (IdpDetails detail : idpDetails) {
                ApprovedTrainings approvedTraining = ApprovedTrainings.builder()
                        .idp(idp)
                        .idpDetail(detail)
                        .idpFlow(flow)
                        .employeeId(employeeId)
                        .training(detail.getTraining())
                        .trainingName(detail.getTrainingName())
                        .year(idp.getYear())
                        .cost(detail.getTraining().getCostPerPersonIndividual())
                        .groupType("individual")
                        .groupCode(detail.getId().toString())
                        .status("Approved")
                        .recordStatus("Active")
                        .build();

                approvedTrainingsDAO.save(approvedTraining);
            }
            generateAndUpdateGroupCodes();
        }

        public void generateAndUpdateGroupCodes() {
            Long yearId = masterYearDAO.getYearIdRunningYear();
            Object[][] eligibleList = approvedTrainingsDAO.getGroupEligibleTrainings(yearId);

            if (eligibleList.length == 0) {
                return; // nothing to update
            }
            for (Object[] row : eligibleList) {

                Long trainingId = row[0] != null ? ((Number) row[0]).longValue() : null;
                Long maxIdpDetailId = row[2] != null ? ((Number) row[2]).longValue() : null;

                // Create group_code (custom pattern) // Example: GRP_{trainingId}_{maxDetailId}
                String groupCode = "GRP_" + trainingId.toString() + "_" + maxIdpDetailId.toString();

                // 3. Update all rows for this training
                approvedTrainingsDAO.updateGroupCode(groupCode, trainingId, yearId);
            }

            // Finally, update total consumed amount in budget config
            approvedTrainingsDAO.updateTotalConsumedAmountByYearId(yearId);
        }
}
