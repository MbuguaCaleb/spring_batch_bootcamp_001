**Important Concepts in SpringBatch**
![img_job.png](img_job.png) 

```
(a)Job
 
(Defines a List of States and Steps and How to transition between one State and the Next)

Spring Batch at the most fundamental level is a State Machine

A Spring Batch Job defines the flow that processing will take through states

Each step reprersents a state within a state machine

A JOB Represents a list of states in the spring BATCH state machine and how
to transition between those states

It takes in Steps.


```

![img_step.png](img_step.png)
```
(b)Step
[Two types of Steps: TaskLet, ChunkStep]
[A tasklet step executes recdursively until it returns: finshed, null or exception

```
![img_tasklet.png](img_tasklet.png)

**Tasklet Step**

```
Is a Single interface with the mehthod execute, and spring boot runs
that single method within the scope of a transaction

[Executes one Single process within the scope of a transaction.]
[anything outside item based processing]

If i want to maybe do one thing in a Job, without extra processing a tasklet
would be ideal.

(eg ftp file, send email, anything outside item based processing.

There are outof thebox tasklets that we can leverage to do our development
(systemCommandlineTaskLet,)

```

![img_chunk_step.png](img_chunk_step.png)

*Chunk Based Step**

```
It is ItemBased

When we look at a chunk based step we expect to be processing items individually.
It has three main components:

(a)Item Reader - Responsible for all the input of the Step.

(b)Item Processor - It is Optional 
[Provides any additional transformation,validation or Logic that 
needs to be applied to each Item.

(c)Item Writer  - Provides the OutPut of the Step

```

**Simple Representation of a Job with Steps**

![img.png](simple_job_represntation.png)

```
The execution of the above job can be step by step,
parallel

```

*Chunk Step Process Flow**
![chunk_step_processing.png](chunk_step_processing.png)

```
(a)First of all we by reading until the chunk  limit is attained .
 [If chunk size is 100, we shall begin first by reading the 100 items]
 
(b)We then Iterate over each of the 100 items with the item processor,

(c) Once all the items have been processed the whole list is passed into the 
item writer, where the write method, will write all of the items in the chunk 
at Once.

[This allows foe Optimization in the Write which we cannot do in these other Steps

[Example, if we are using jdbc we can do a batch insert]

This process is repeated until all the input is exhausted
Therefore if i have 1000 items, i will have 10 chunks, if each chunk has 100 items

```
![job_repository_flow.png](job_repository_flow.png)

**Job Repository**

```
As the Job transitions between the various phases of a Job, State is maintained,
State is maintained in a Job Repository

Out of the Box, Spring provides two repositories,

(a)Mapped One - Testing Puproses
(b)JDBC One - Relational Database

Data stored in Job repository is provided by the various components.
If something goes wrong,we are able to restart at the step where the error occured

```
*Notes By*

```
MbuguaCaleb

```