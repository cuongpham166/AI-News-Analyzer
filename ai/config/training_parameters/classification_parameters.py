classification_training_params_old_cpu = {
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

classification_training_params_new_cpu = {
    "output_dir":"./ai/models",
    "per_device_train_batch_size":8,
    "per_device_eval_batch_size":8,
    "gradient_accumulation_steps":2,  # lower because batch is larger
    "num_train_epochs":3,             # Ryzen can handle more epochs
    "learning_rate":2e-5, #If accuracy drops: reduce learning rate slightly → 1.5e-5
    "eval_strategy":"epoch",
    "save_strategy":"epoch",
    "logging_steps":100,
    "load_best_model_at_end":True,
    "fp16":False,                    
    "dataloader_num_workers":4,       # IMPORTANT upgrade (i5 = 0, Ryzen = 4)
    "dataloader_pin_memory":True,
    "report_to":"none"
}