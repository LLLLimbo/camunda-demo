package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.OWNER_MAINTAIN_PROCESS_INSTANCE_ID;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY;
import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageStaffConnected implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("客服已进行事前沟通-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String ownerMaintainInstanceId = delegateExecution.getVariablesTyped()
        .getValue(OWNER_MAINTAIN_PROCESS_INSTANCE_ID, String.class);

    log.info("价格 {} 元", delegateExecution.getVariable("price"));

    EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(ownerMaintainInstanceId)
        .eventType("message")
        .eventName("MessageStaffConnected")
        .singleResult();

    Map<String,Object> dmnOutput = delegateExecution.getVariablesTyped()
        .getValue("accessResult", Map.class);

    delegateExecution.setVariables(dmnOutput);

    delegateExecution
        .setVariable(STAFF_CENTER_PROCESS_INSTANCE_ID, delegateExecution.getProcessInstanceId());
    delegateExecution.setVariable(STAFF_CENTER_DEPT_PROCESS_BUSINESS_KEY,
        delegateExecution.getProcessBusinessKey());

    runtimeService.messageEventReceived(subscription.getEventName(), subscription.getExecutionId(),
        delegateExecution.getVariables());
  }

}
