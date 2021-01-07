package com.example.workflow.bpmn.maintain;

import static com.example.workflow.bpmn.maintain.MaintainProcessFieldName.STAFF_CENTER_PROCESS_INSTANCE_ID;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.runtime.EventSubscription;

@Slf4j
public class MessageOwnerVerified implements JavaDelegate {

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {
    log.info("业主验收通过-消息事件触发");

    RuntimeService runtimeService = delegateExecution.getProcessEngine().getRuntimeService();

    String staffInstanceId = delegateExecution.getVariablesTyped()
        .getValue(STAFF_CENTER_PROCESS_INSTANCE_ID, String.class);

    EventSubscription subscription = runtimeService.createEventSubscriptionQuery()
        .processInstanceId(staffInstanceId)
        .eventType("message")
        .eventName("MessageOwnerVerified")
        .singleResult();

    //触发客户服务中心流程中的消息事件
    runtimeService.messageEventReceived(subscription.getEventName(), subscription.getExecutionId());

    //消息启动回访流程
    runtimeService
        .startProcessInstanceByMessage("MessageOwnerVerified", "MaintainProcess_ReturnVisit",
            delegateExecution.getVariables());
  }
}
