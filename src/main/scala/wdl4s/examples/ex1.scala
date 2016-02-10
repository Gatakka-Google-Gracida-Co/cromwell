package wdl4s.examples
import wdl4s.NamespaceWithWorkflow

object ex1 {
  def main(args: Array[String]) {
    val wdl = """
    |task a {
    |  command { ps }
    |}
    |workflow wf {
    | call a
    |}""".stripMargin

    val ns = NamespaceWithWorkflow.load(wdl)

    println(s"Workflow: ${ns.workflow.unqualifiedName}")
    ns.workflow.calls foreach {call =>
      println(s"Call: ${call.unqualifiedName}")
    }

    ns.tasks foreach {task =>
      println(s"Task: ${task.name}")
      println(s"Command: ${task.commandTemplate}")
    }
  }
}
