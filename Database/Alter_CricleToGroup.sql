ALTER TABLE `groupusers`
  CHANGE `cricleseq` `groupseq` int;
  
RENAME TABLE `circles` TO `groups`;

RENAME TABLE `cricleusers` TO `groupuserss`;
