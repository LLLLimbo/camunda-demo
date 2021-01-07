package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.MAINTAINER_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageWarehouseMatSend implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("物料已发放-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String maintainerProcessInstanceId = delegateExecution.getVariablesTyped()
        .getValue(MAINTAINER_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(maintainerProcessInstanceId)
        .eventType("message")
        .eventName("MessageWarehouseMatSend")
        .singleResult();

    runtimeService
        .messageEventReceived(subscription.getEventName(), subscription.getExecutionId(),
            delegateExecution.getVariables());
  }
}
