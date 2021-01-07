package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageMaintainFinishedOwner implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("维修完成(业主)-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String ownerMaintainProcessInstanceId = delegateExecution.getVariablesTyped()
        .getValue(OWNER_MAINTAIN_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscriptionInOwner = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(ownerMaintainProcessInstanceId)
        .eventType("message")
        .eventName("MessageMaintainFinishedOwner")
        .singleResult();

    runtimeService
        .messageEventReceived(subscriptionInOwner.getEventName(),
            subscriptionInOwner.getExecutionId(),
            delegateExecution.getVariables());
  }
}
