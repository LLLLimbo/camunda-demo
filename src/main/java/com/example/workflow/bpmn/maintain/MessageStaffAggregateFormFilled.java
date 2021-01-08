package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessBusinessKeyPrefix.MAINTAIN_PROCESS_MAINTAIN_DEPT_MANAGER;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAIN_DEPT_MANAGER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAIN_DEPT_MANAGER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_STAFF_AGG_FORM_FILLED;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageStaffAggregateFormFilled implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("工程单汇总表已填写-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String ownerInstanceId = delegateExecution.getVariablesTyped()
        .getValue(OWNER_MAINTAIN_PROCESS_INSTANCE_ID, String.class);
    String ownerInstanceKey = delegateExecution.getVariablesTyped()
        .getValue(OWNER_MAINTAIN_PROCESS_BUSINESS_KEY, String.class);

    delegateExecution
        .setVariable(STAFF_CENTER_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution.setVariable(STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY,
        delegateExecution.getProcessBusinessKey());
    delegateExecution
        .setVariable(OWNER_MAINTAIN_PROCESS_INSTANCE_ID, ownerInstanceId);
    delegateExecution.setVariable(OWNER_MAINTAIN_PROCESS_BUSINESS_KEY,
        ownerInstanceKey);

    ProcessInstance ownerMaintainProcessInstance = runtimeService
        .startProcessInstanceByMessage(MSG_STAFF_AGG_FORM_FILLED,
            MAINTAIN_PROCESS_MAINTAIN_DEPT_MANAGER + delegateExecution.getCurrentActivityId(),
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(MAINTAIN_DEPT_MANAGER_PROCESS_INSTANCE_ID,
            ownerMaintainProcessInstance.getId());
    delegateExecution
        .setVariable(MAINTAIN_DEPT_MANAGER_PROCESS_BUSINESS_KEY,
            ownerMaintainProcessInstance.getBusinessKey());
  }
}
