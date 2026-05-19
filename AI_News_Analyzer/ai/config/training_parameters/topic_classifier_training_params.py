topic_classifier_training_params_old_cpu = {
    "output_dir":"./ai/models",
    "per_device_train_batch_size":2,
    "per_device_eval_batch_size":2,
    "gradient_accumulation_steps":8,
    "num_train_epochs":2,        
    "learning_rate":2e-5,
    "eval_strategy":"epoch",
    "save_strategy":"epoch",
    "logging_steps":50,
    "load_best_model_at_end":True,
    "fp16":False,                
    "dataloader_num_workers":0,  
    "report_to":"none"
}

topic_classifier_training_params_new_cpu = {
    "output_dir":"./ai/models",
    "overwrite_output_dir":True,

    "per_device_train_batch_size":8,
    "per_device_eval_batch_size":8,
    "gradient_accumulation_steps":2,  # lower because batch is larger

    # Training
    "num_train_epochs":5,
    "learning_rate":1e-5,
    "weight_decay":0.01,
    "warmup_ratio":0.1,
    "max_grad_norm":1.0,

    # Evaluation
    "eval_strategy":"epoch",
    "save_strategy":"epoch",
    "logging_strategy":"steps",
    "logging_steps":50,

    # Best model
    "load_best_model_at_end":True,
    "metric_for_best_model":"eval_loss",
    "greater_is_better":False,

    # CPU
    "fp16":False,
    "dataloader_num_workers":4,
    "dataloader_pin_memory":False,

    # Misc
    "report_to":"none",
    "seed":42
}