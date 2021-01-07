package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAIN_DEPT_MANAGER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;
import org.camunda.bpm.engine.runtime.ProcessInstance;

@Slf4j
public class MessageOwnerReworkStaff implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("业主验收未通过-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String staffInstanceId = delegateExecution.getVariablesTyped()
        .getValue(STAFF_CENTER_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscriptionInStaff = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(staffInstanceId)
        .eventType("message")
        .eventName("MessageOwnerReworkStaff")
        .singleResult();

    runtimeService.messageEventReceived(subscriptionInStaff.getEventName(),
        subscriptionInStaff.getExecutionId());
  }
}
