package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessMessageNames.MSG_MAINTAIN_FINISHED_STAFF;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageMaintainFinishedStaff implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("维修完成-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String staffCenterInstanceId = delegateExecution.getVariablesTyped()
        .getValue(STAFF_CENTER_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscriptionInStaffCenter = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(staffCenterInstanceId)
        .eventType("message")
        .eventName(MSG_MAINTAIN_FINISHED_STAFF)
        .singleResult();

    runtimeService
        .messageEventReceived(subscriptionInStaffCenter.getEventName(),
            subscriptionInStaffCenter.getExecutionId(),
            delegateExecution.getVariables());
  }
}
