package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessBusinessKeyPrefix.MAINTAIN_PROCESS_WAREHOUSE;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.WAREHOUSE_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.WAREHOUSE_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_MAT_APPLIED;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageMatApplied implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("物料已申请-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    delegateExecution
        .setVariable(MAINTAINER_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution
        .setVariable(MAINTAINER_PROCESS_BUSINESS_KEY, delegateExecution.getProcessBusinessKey());

    ProcessInstance adminDeptStageInstance = runtimeService
        .startProcessInstanceByMessage(MSG_MAT_APPLIED,
            MAINTAIN_PROCESS_WAREHOUSE + delegateExecution.getCurrentActivityId(),
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(WAREHOUSE_PROCESS_INSTANCE_ID, adminDeptStageInstance.getId());
    delegateExecution
        .setVariable(WAREHOUSE_PROCESS_BUSINESS_KEY, adminDeptStageInstance.getBusinessKey());
  }
}
