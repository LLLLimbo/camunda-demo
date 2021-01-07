package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageOwnerConfirmContinuation implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("业主确认是否继续-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String staffInstanceId = delegateExecution.getVariablesTyped()
        .getValue(STAFF_CENTER_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(staffInstanceId)
        .eventType("message")
        .eventName("MessageOwnerConfirmContinuation")
        .singleResult();

    runtimeService
        .messageEventReceived(subscription.getEventName(), subscription.getExecutionId(),
            delegateExecution.getVariables());
  }
}
