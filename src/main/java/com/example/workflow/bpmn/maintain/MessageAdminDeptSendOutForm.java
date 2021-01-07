package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.ADMIN_DEPT_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageAdminDeptSendOutForm implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("行政部维修单已登记下发-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    delegateExecution
        .setVariable(ADMIN_DEPT_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution
        .setVariable(ADMIN_DEPT_PROCESS_BUSINESS_KEY, delegateExecution.getProcessBusinessKey());

    ProcessInstance adminDeptStageInstance = runtimeService
        .startProcessInstanceByMessage("MessageAdminDeptSendOutForm", "MaintainProcess_StaffCenter",
            delegateExecution.getVariables());

    delegateExecution
        .setVariable(STAFF_CENTER_PROCESS_INSTANCE_ID, adminDeptStageInstance.getId());
    delegateExecution
        .setVariable(STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY, adminDeptStageInstance.getBusinessKey());
  }
}
